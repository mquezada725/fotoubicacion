package com.example.fotoubicacion;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fotoubicacion.Adaptadores.AdaptadorFotos;
import com.example.fotoubicacion.DB.DBHelper;
import com.example.fotoubicacion.DB.FotosDB;
import com.example.fotoubicacion.Entidades.ListadoFoto;
import com.example.fotoubicacion.Models.Fotos;




import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GenerarFotos extends AppCompatActivity {

    Button BtnCamaraTomar,BtnAvanzarFormulario, BtnRegresarMenu, BtnAgregarGaleria;
    Uri uri;
    ImageView ImgCamara;
    TextView UbicacionEscrita,Comentario;
    OutputStream OutStream;
    FotosDB fotosDB;
    Fotos fotosget;
    SQLiteDatabase Sqlsolicitud;
    private static final int PLACE_PICKER_REQUEST = 1;
    private final int GALLERY_REQ_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_fotos);
        BtnCamaraTomar = findViewById(R.id.BtnAccederCamara);
        ImgCamara = findViewById(R.id.ImgFotoCaptura);
        UbicacionEscrita = findViewById(R.id.TxtUbicacion);
        BtnAvanzarFormulario = findViewById(R.id.BtnGuardarFoto);
        BtnRegresarMenu = findViewById(R.id.BtnRegresar);
        BtnAgregarGaleria = findViewById(R.id.BtnCargarGaleria);
        Comentario = findViewById(R.id.TxtComentario);

        getSupportActionBar().hide();



        //Boton para obtnener ubicacion


        //Boton que realiza validacion de foto, ubicacion y comentario y almacena la foto tomada
        BtnAvanzarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent IntentoFormular = new Intent(GenerarFotos.this,MainActivity.class);

                if(ImgCamara.getDrawable() == null || UbicacionEscrita.getText().toString().length() == 0){
                    Toast.makeText(GenerarFotos.this, "Por Favor Agrege Una Foto o Agrege Ubicación", Toast.LENGTH_SHORT).show();
                }else{
                    BitmapDrawable GuardarFotoDirectorio = (BitmapDrawable) ImgCamara.getDrawable();
                    Bitmap Bitmapeado = GuardarFotoDirectorio.getBitmap();

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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        OutStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FotosDB dbfoto = new FotosDB(GenerarFotos.this);
                    try {
                        //dbfoto.InsertarFotoDB((ImgCamara.getDrawable()),UbicacionEscrita.getText().toString(),Comentario.getText().toString());
                        Fotos fotosprueba = new Fotos();
                        fotosprueba.setImagenTomada(Archivar.getPath());
                        fotosprueba.setUbicacionSolicitada(UbicacionEscrita.getText().toString());
                        fotosprueba.setComentarioFoto(Comentario.getText().toString());
                        dbfoto.InsertarFotoDB(fotosprueba);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(IntentoFormular);
                    Toast.makeText(GenerarFotos.this,"Foto Guardada Con Exito",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Boton que inicia el metodo para comenzar la aplicacion de camara
        BtnCamaraTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CamaraLanzar.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));

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
    ActivityResultLauncher<Intent> CamaraLanzar = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Bundle extra = result.getData().getExtras();
                Bitmap ImgBitMap = (Bitmap) extra.get("data");
                ImgCamara.setImageBitmap(ImgBitMap);

            }
        }

    });




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQ_CODE){
                ImgCamara.setImageURI(data.getData());
            }

        }
    }



}
