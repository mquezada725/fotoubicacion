package com.example.fotoubicacion.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "foto_ubicacion.db";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLite) {
        sqLite.execSQL(TABLA_FORMULARIO);
        sqLite.execSQL(TABLA_FOTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //String SolSiExiste = "DROP TABLE IF EXISTS " + CREAR_TABLA_FORMULARIO + "";
        //String SolSiExiste1 = "DROP TABLE IF EXISTS" + CREAR_TABLA_FOTOS + "";
        //sqLiteDatabase.execSQL(SolSiExiste);
        //sqLiteDatabase.execSQL(SolSiExiste1);

    }

    public static final String TABLA_FORMULARIO = " CREATE TABLE formulario_recorrido (" +
            "ID_FORMULARIO INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "TITULO_FORMULARIO NVARCHAR (200) NOT NULL ," +
            "FECHA_FORMULARIO FECHA NOT NULL ," +
            "NUMEROEVENTO INTEGER NOT NULL," +
            "SUPERVISORTURNO TEXT NOT NULL," +
            "TECNICOREPARACION TEXT NOT NULL," +
            "TECNICOREPARACION2 TEXT ," +
            "ZONAMANTENIMIENTO TEXT NOT NULL, " +
            "FECHAINICIOHORAACT FECHA NOT NULL, " +
            "FECHATERMINOHORAACT FECHA NOT NULL," +
            "FECHATOTAL FECHA NOT NULL," +
            "CLIENTEAFECTADO TEXT NOT NULL," +
            "TIPOCLIENTE TEXT NOT NULL, " +
            "LOCALIZACIONFALLA NVARCHAR(150) NOT NULL," +
            "CERRARMODIFICACION TEXT NOT NULL," +
            "DESCRIPCIONMATERIAL NVARCHAR (150)," +
            "DESCRIPCIONTRABAJO NVANCHAR (150)," +
            "RESOLUCIONTRABAJO NVARCHAR (150)," +
            "OBSERVACION NVARHCAR(150)" + ");";

    public static final String TABLA_FOTOS = " CREATE TABLE foto_listado (" +
            "ID_FOTO INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "ID_FOTO_TOMADA VARCHAR(200) NOT NULL,"+
            "ID_UBICACION NVARCHAR (200)," +
            "ID_COMENTARIO NVANCHAR (150)" + ");";
}

//"DISTANCIAOPTICA NVARCHAR(200) NOT NULL," +
//"LATITUD DECIMAL (10,5) NOT NULL," +
//"LONGITUD DECIMAL (10,5) NOT NULL," +
//"REGISTROFOTOANTES TEXT NOT NULL," +
//"REGISTROFOTOFINAL TEXT NOT NULL," +
//"REGISTROMEDICIONANTES TEXT NOT NULL," +
//"REGISTROMEDICIONFINAL TEXT NOT NULL," +
//"PERDIDASERVICIO TEXT NOT NULL," +
//"ASOCIADOEVENTO TEXT NOT NULL ," +