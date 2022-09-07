package com.example.fotoubicacion;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fotoubicacion.Adaptadores.AdaptadorFotos;
import com.example.fotoubicacion.DB.DBHelper;
import com.example.fotoubicacion.DB.FotosDB;
import com.example.fotoubicacion.Entidades.ListadoFoto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GenerarFotos extends AppCompatActivity {

    Button BtnCamaraTomar,BtnAvanzarFormulario, BtnRegresarMenu;
    ImageView ImgCamara;
    TextView UbicacionEscrita;
    TextView Comentario;
    OutputStream OutStream;
    FotosDB fotosDB;
    SQLiteDatabase Sqlsolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_fotos);
        BtnCamaraTomar= findViewById(R.id.BtnAccederCamara);
        ImgCamara=findViewById(R.id.ImgFotoCaptura);
        UbicacionEscrita=findViewById(R.id.TxtUbicacion);
        BtnAvanzarFormulario = findViewById(R.id.BtnGuardarFoto);
        BtnRegresarMenu = findViewById(R.id.BtnRegresar);
        Comentario = findViewById(R.id.TxtComentario);
        getSupportActionBar().hide();


        BtnRegresarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IntentoRegresoMenu = new Intent(GenerarFotos.this, MainActivity.class);
                startActivity(IntentoRegresoMenu);
            }
        });

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
                        dbfoto.InsertarFotoDB("/Dcim",UbicacionEscrita.getText().toString(),Comentario.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(IntentoFormular);
                    Toast.makeText(GenerarFotos.this,"Foto Guardada Con Exito",Toast.LENGTH_SHORT).show();
                }

            }
        });

        BtnCamaraTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CamaraLanzar.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            }
        });
    }


    ActivityResultLauncher<Intent> CamaraLanzar = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Bundle extra = result.getData().getExtras();
                Bitmap ImgBitMap = (Bitmap) extra.get("data");
                ImgCamara.setImageBitmap(ImgBitMap);

            }
        }

    });
}