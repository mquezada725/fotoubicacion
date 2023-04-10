package com.example.fotoubicacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.fotoubicacion.Adaptadores.AdaptadorFotos;
import com.example.fotoubicacion.DB.DBHelper;
import com.example.fotoubicacion.DB.FotosDB;
import com.example.fotoubicacion.Entidades.FotosModels;

import java.util.ArrayList;

public class EditarRecorrido extends AppCompatActivity {

    // Llamar clases de base de datos locales
    DBHelper BaseFotos;
    SQLiteDatabase dbFotos;
    AdaptadorFotos adfotos;
    RecyclerView ListaVista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_recorrido);
        getSupportActionBar().hide();
        //Referencias

        FotosDB db = new FotosDB(EditarRecorrido.this);

        BaseFotos = new DBHelper(this);
        ListaVista = findViewById(R.id.RvListado);
        ListaVista.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        DisplayListado();
    }

    private void DisplayListado() {
        dbFotos = BaseFotos.getReadableDatabase();

        Cursor Curseado = dbFotos.rawQuery(" SELECT * FROM foto_listado " ,null);
        ArrayList<FotosModels> ModeloFoto = new ArrayList<>();
        while (Curseado.moveToNext()){
            int IDfoto = Curseado.getInt(0);
            String PathFoto = Curseado.getString(1);
            byte[] ImagenFoto = Curseado.getBlob(2);
            if (ImagenFoto.length==0) {ImagenFoto = new byte[0];}
            String UbicacionEscrita = Curseado.getString(3);
            String ComentarioOp = Curseado.getString(4);
            ModeloFoto.add(new FotosModels(IDfoto,PathFoto,ImagenFoto,UbicacionEscrita,ComentarioOp));

        }
        Curseado.close();
        adfotos = new AdaptadorFotos(getApplicationContext(),R.layout.muestrafotodato,ModeloFoto);
        ListaVista.setAdapter(adfotos);
    }



}
