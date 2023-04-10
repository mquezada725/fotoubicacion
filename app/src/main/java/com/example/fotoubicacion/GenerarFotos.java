package com.example.fotoubicacion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fotoubicacion.DB.FotosDB;
import com.example.fotoubicacion.Models.Fotos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.UUID;

public class GenerarFotos extends AppCompatActivity {

    Button BtnCamaraTomar,BtnGuardarFoto, BtnRegresarMenu, BtnAgregarGaleria;
    Uri uri;
    ImageView ImgCamara;
    TextView UbicacionEscrita,Comentario;
    FotosDB fotosDB;
    Fotos fotosget;
    Tools tools;
    OutputStream OutStream;
    SQLiteDatabase Sqlsolicitud;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static String FOTO_RUTA_KEY = "Key_1";
    private final int GALLERY_REQ_CODE = 1000;
    private String filepath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_fotos);
        BtnCamaraTomar = findViewById(R.id.BtnAccederCamara);
        ImgCamara = findViewById(R.id.ImgFotoCaptura);
        UbicacionEscrita = findViewById(R.id.TxtUbicacion);
        BtnGuardarFoto = findViewById(R.id.BtnGuardarFoto);
      //  BtnRegresarMenu = findViewById(R.id.BtnRegresar);
        BtnAgregarGaleria = findViewById(R.id.BtnCargarGaleria);
        Comentario = findViewById(R.id.TxtComentario);

        getSupportActionBar().hide();
        Bundle extras = getIntent().getExtras();

        if(extras != null){

            FOTO_RUTA_KEY = extras.getString("RUTA_FOTO");

            //File sd = Environment.getExternalStorageDirectory();
            //File image = new File(FOTO_RUTA_KEY, String.valueOf(UUID.randomUUID()));
            Bitmap bitmap = BitmapFactory.decodeFile(FOTO_RUTA_KEY);
            //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            //Bitmap bitmap = BitmapFactory.decodeFile(image.getPath(),bmOptions);

            ImgCamara.setImageBitmap(bitmap);
        }

        //Boton para obtnener ubicacion

        //Boton que realiza validacion de foto, ubicacion y comentario y almacena la foto tomada
        BtnGuardarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(UbicacionEscrita.getText().toString().length() == 0 &&
                                FOTO_RUTA_KEY.equals("Key_1")){
                    Toast.makeText(GenerarFotos.this, "Por Favor Agrege Foto y Ubicación", Toast.LENGTH_SHORT).show();
                }else{

                  /*  BitmapDrawable drawtobmp = (BitmapDrawable) ImgCamara.getDrawable();
                    Bitmap Bitmapeado = drawtobmp.getBitmap();

                    File ArchivoFoto = Environment.getExternalStorageDirectory();
                    File Directorio = new File(ArchivoFoto.getAbsolutePath() +"/Dcim");
                    if(Directorio.exists()) {
                        Directorio.mkdir();
                    }
                    File Archivar = new File(Directorio, System.currentTimeMillis() + ".jpg");
                    try {
                        OutStream = new FileOutputStream(Archivar);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Boolean Exito = Bitmapeado.compress(Bitmap.CompressFormat.JPEG,100,OutStream);
                    if(!Exito){
                        Bitmap ClonarImagen = Bitmap.createScaledBitmap(Bitmapeado,Bitmapeado.getWidth(),Bitmapeado.getHeight(),false);
                        OutStream = new ByteArrayOutputStream();
                        ClonarImagen.compress(Bitmap.CompressFormat.JPEG,100,OutStream);
                    }
                    try {
                        OutStream.flush();
                        OutStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

/*                    try {
                        OutStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                 /*   Bitmap resizedBitmap = getResizedBitmap(Bitmapeado, 1000);
                    ByteArrayOutputStream baOutStream = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baOutStream);
                    byte[] byteArray = baOutStream.toByteArray();*/

                    //File sd = Environment.getExternalStorageDirectory();
 /*                   File image = new File(FOTO_RUTA_KEY, String.valueOf(UUID.randomUUID()));
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();*/
                    Bitmap bitmap = BitmapFactory.decodeFile(FOTO_RUTA_KEY);

                    //Bitmap resizedBitmap = getResizedBitmap(bitmap, 1000);
                    ByteArrayOutputStream baOutStream = new ByteArrayOutputStream();
                    //resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baOutStream);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baOutStream);
                    byte[] byteArray = baOutStream.toByteArray();

                    FotosDB dbfoto = new FotosDB(GenerarFotos.this);
                    try {
                        Fotos fotosprueba = new Fotos();
                        fotosprueba.setPathImagen(FOTO_RUTA_KEY);
                        fotosprueba.setImagenReal(byteArray);
                        fotosprueba.setUbicacionSolicitada(UbicacionEscrita.getText().toString());
                        fotosprueba.setComentarioFoto(Comentario.getText().toString());
                        dbfoto.InsertarFotoDB(fotosprueba);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent i = new Intent(MainActivity.getContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

                    Toast.makeText(GenerarFotos.this,"Foto Guardada Con Exito",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Boton que inicia el metodo para comenzar la aplicacion de camara
        BtnCamaraTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //CamaraLanzar.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                Intent IntentoFoto = new Intent(GenerarFotos.this, CameraXActivity.class);
                startActivity(IntentoFoto);
            }
        });

        //Boton para tomar foto desde galeria
        BtnAgregarGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeria = new Intent(Intent.ACTION_PICK);
                galeria.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeria, GALLERY_REQ_CODE);
            }
        });
    }



    //Metodo para iniciar la ejecucion de la camara del dispositivo android
/*    ActivityResultLauncher<Intent> CamaraLanzar = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Bundle extra = result.getData().getExtras();
                Bitmap ImgBitMap = (Bitmap) extra.get("data");
                ImgCamara.setImageBitmap(ImgBitMap);
            }
        }

    });*/

    //RESPUESTA FOTO TOMADA DESDE GALERIA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQ_CODE){
                ImgCamara.setImageURI(data.getData());
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
