package com.example.fotoubicacion;

import static com.example.fotoubicacion.DB.DBHelper.TABLA_FOTOS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;

import com.example.fotoubicacion.Adaptadores.AdaptadorFotos;
import com.example.fotoubicacion.DB.DBHelper;
import com.example.fotoubicacion.DB.FotosDB;
import com.example.fotoubicacion.Entidades.ListadoFoto;

import java.util.ArrayList;
import java.util.List;

public class EditarRecorrido extends AppCompatActivity {

    // Llamar clases de base de datos locales
    DBHelper BaseFotos;
    SQLiteDatabase sqLiteDatabase;
    // Creacion adaptador para fotos tomadas
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
        findID();
        DisplayListado();
        ListaVista.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

    }

    private void DisplayListado() {
        sqLiteDatabase = BaseFotos.getReadableDatabase();
        Cursor Curseado = sqLiteDatabase.rawQuery(" SELECT * FROM foto_listado " ,null);
        ArrayList<ListadoFoto> ModeloFoto = new ArrayList<>();
        while (Curseado.moveToNext()){
            int IDtomar = Curseado.getInt(0);
            byte[]ImagenFoto = Curseado.getBlob(1);
            String UbicacionEscrita = Curseado.getString(2);
            String ComentarioOp = Curseado.getString(3);
            ModeloFoto.add(new ListadoFoto(IDtomar,ImagenFoto,"Ubicación Fotografia : "+UbicacionEscrita,
                    "Comentario Foto : " +ComentarioOp));

        }
        Curseado.close();
        adfotos = new AdaptadorFotos(this,R.layout.muestrafotodato,ModeloFoto,sqLiteDatabase);
        ListaVista.setAdapter(adfotos);
    }

    private void findID() {
        ListaVista = findViewById(R.id.RvListado);
    }
}