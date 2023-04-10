package com.example.fotoubicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.ZoomState;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.SensorManager;
import android.media.Image;
import android.media.MediaActionSound;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraXActivity extends AppCompatActivity {

    private Executor executor = Executors.newSingleThreadExecutor();
    private int REQUEST_CODE_PERMISSIONS=1001; // , rotation=0
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};
    private AlertDialog alertDialog;
    private PreviewView previewView;
    private Button btnCaptureImage, btnBackNav;
    private OrientationEventListener myOrientationEventListener;
    private ProgressDialog progressDialog;
    private TextView txtOrientation, viewShootCount;
    private CameraSelector cameraSelector;
    private ImageAnalysis imageAnalysis;
    private ImageCapture imageCapture;
    private Preview preview;
    private Camera camera;
    private int rotation = 0, fotos=0;
    private Bundle extras;
    private String numpedido;
    private Handler handler = new Handler();
    private Runnable runnable;
    private Tools tools;
    private EditText editRecibidoPor;
    CameraControl cameraControl;
    CameraInfo cameraInfo;
    private TextView viewPosfoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_xactivity);
        getSupportActionBar().hide();
        tools = new Tools(CameraXActivity.this);
        viewPosfoto = findViewById(R.id.sensor_posicionfoto);
        progressDialog = new ProgressDialog(CameraXActivity.this);
        previewView = findViewById(R.id.previewView);
        btnCaptureImage = findViewById(R.id.btn_captura_foto);
        btnBackNav = findViewById(R.id.btn_back_nav_cam);
        btnBackNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IntentoFoto = new Intent(CameraXActivity.this, GenerarFotos.class);
                startActivity(IntentoFoto);
            }
        });

        alertDialog = new AlertDialog.Builder(CameraXActivity.this).create();

        if(allPermissionsGranted()){
            startCamera(); //start camera if permission has been granted by user
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

    }

    private void startCamera(){
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(CameraXActivity.this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(CameraXActivity.this));
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private Bitmap imageToBitmap(Image image){
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
        return bitmap;
    }

    @SuppressLint("RestrictedApi")
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider){
        //
        //extras = getIntent().getExtras();
        btnCaptureImage.setVisibility(View.VISIBLE);
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        if(hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)){
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        imageAnalysis = new ImageAnalysis.Builder().build();
        preview = new Preview.Builder().build();
        imageCapture = new ImageCapture.Builder()
                .setMaxResolution(new Size(800, 600))
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        //int rotation = 0;
        OrientationEventListener orientationEventListener = new OrientationEventListener(CameraXActivity.this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {

                viewPosfoto.setText(""+orientation);

                // Monitors orientation values to determine the target rotation value
                //int rotation = 0;
                if (orientation >= 45 && orientation < 135) {
                    //rotation = Surface.ROTATION_270;
                    rotation = 180;
                } else if (orientation >= 135 && orientation < 270) {
                    //rotation = Surface.ROTATION_180; //posicion izq
                    rotation = 360;
                } else if (orientation >= 225 && orientation < 370) {
                    //rotation = Surface.ROTATION_90;
                    rotation = 90;
                } else {
                    //rotation = Surface.ROTATION_0;
                    rotation = 0;
                }
            }
        };

        ScaleGestureDetector.OnScaleGestureListener listener = new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                ZoomState f = camera.getCameraInfo().getZoomState().getValue();
                Log.d("Zoom", String.valueOf(f.getZoomRatio()));

                float scale = scaleGestureDetector.getScaleFactor();
                camera.getCameraControl().setZoomRatio(scale * f.getZoomRatio());
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

            }
        };

        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getApplicationContext(), listener);

        previewView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return scaleGestureDetector.onTouchEvent(motionEvent);

            }
        });

        orientationEventListener.enable();
        camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);
        preview.setSurfaceProvider(previewView.createSurfaceProvider());
        /** CAPTURA IMAGEN CAMARA TRASERA*/
        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File ArchivoFoto = Environment.getExternalStorageDirectory();
                File Directorio = new File(ArchivoFoto.getAbsolutePath() +"/DCIM/Camera");
                if(Directorio.exists()) {
                    Directorio.mkdir();
                }
                File Archivar = new File(Directorio, System.currentTimeMillis() + ".jpg");

                imageCapture.takePicture(executor, new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        shootSound();
                        @SuppressLint("UnsafeOptInUsageError")
                        //Bitmap imageBitmap = rotateImage(imageToBitmap(Objects.requireNonNull(image.getImage())), rotation);
                        Bitmap imageBitmap = imageToBitmap(Objects.requireNonNull(image.getImage()));
                        try {
                            scanner(Archivar.getPath());
                            OutputStream fos = new FileOutputStream(Archivar);
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            Objects.requireNonNull(fos).close();
                            Intent IntentoFoto = new Intent(CameraXActivity.this, GenerarFotos.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("RUTA_FOTO", Archivar.getPath());
                            IntentoFoto.putExtras(bundle);
                            startActivity(IntentoFoto);
                            //viajesData.insertRutaFoto(numpedido, file2.toString());
                            //fotos++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        super.onCaptureSuccess(image);
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        super.onError(exception);
                    }
                });

            }
        });
    }

    private void scanner(String path) {
        MediaScannerConnection.scanFile(CameraXActivity.this,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }

    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera();
            } else{
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //finalizaCamara();
    }


    public void shootSound() {
        MediaActionSound sound = new MediaActionSound();
        sound.play(MediaActionSound.SHUTTER_CLICK);
    }
}