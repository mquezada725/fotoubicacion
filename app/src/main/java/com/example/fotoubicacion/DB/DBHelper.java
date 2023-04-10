package com.example.fotoubicacion.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
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
    public void onUpgrade(SQLiteDatabase sqLite, int i, int i1) {
        String SolSiExiste1 = "DROP TABLE IF EXISTS formulario_recorrido ";
        sqLite.execSQL(SolSiExiste1);
        String SolSiExiste2 = "DROP TABLE IF EXISTS foto_listado ";
        sqLite.execSQL(SolSiExiste2);
        onCreate(sqLite);
    }

    public static final String TABLA_FORMULARIO = " CREATE TABLE formulario_recorrido (" +
            "ID_FORMULARIO INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "REGISTRO INTEGER NOT NULL," +
            "TITULO_FORMULARIO NVARCHAR (200) NOT NULL ," +
            "FECHA_FORMULARIO FECHA NOT NULL ," +
            "SUPERVISORTURNO TEXT NOT NULL," +
            "TECNICOREPARACION TEXT NOT NULL," +
            "TECNICOREPARACION2 TEXT ," +
            "ZONAMANTENIMIENTO TEXT NOT NULL, " +
            "FECHAINICIOHORAACT FECHA NOT NULL, " +
            "FECHATERMINOHORAACT FECHA NOT NULL," +
            "FECHATOTAL FECHA NOT NULL," +
            "CLIENTEAFECTADO TEXT NOT NULL," +
            "REFERENCIACLIENTE TEXT NOT NULL," +
            "TIPOCLIENTE TEXT NOT NULL, " +
            "LOCALIZACIONFALLA NVARCHAR(150) NOT NULL," +
            "DESCRIPCIONMATERIAL NVARCHAR (150)," +
            "DESCRIPCIONTRABAJO NVANCHAR (150)," +
            "RESOLUCIONTRABAJO NVARCHAR (150)," +
            "OBSERVACION NVARHCAR(150)" + ");";

    public static final String TABLA_FOTOS = " CREATE TABLE foto_listado (" +
            "ID_FOTO INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "ID_FOTO_TOMADA VARCHAR(200) NOT NULL,"+
            "IMAGEN_REAL BLOB,"+
            "ID_UBICACION NVARCHAR (200)," +
            "ID_COMENTARIO NVANCHAR (150)" + ");";
}
