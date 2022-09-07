package com.example.fotoubicacion.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fotoubicacion.Entidades.ListadoFoto;

import java.util.ArrayList;
import java.util.List;

public class FotosDB {
    private SQLiteDatabase DbFotos;
    private DBHelper DBHelp;


    public FotosDB(Context Contenido){
            DBHelp = new DBHelper(Contenido);
    }


    public void Abertura(){
        DbFotos = DBHelp.getWritableDatabase();
    }

    public void Clausura(){
        DBHelp.close();
    }

    public void InsertarFotoDB(String ImagenTomada, String UbicacionSolicitada, String ComentarioFoto) throws Exception{
        int Ultimo_id = 0;
        try{
            String Solicitud = "INSERT INTO fotoTomada ( " + " ID_FOTO_TOMADA, ID_UBICACION, ID_COMENTARIO) VALUES (?,?,?)";
            DbFotos.execSQL(Solicitud, new Object[]{ImagenTomada,UbicacionSolicitada,ComentarioFoto});
        }catch (Exception MensajeERROR){
            throw new Exception(MensajeERROR.getMessage());
        }
    }

    public List<ListadoFoto> CargarListadoFoto(int id_fotos, byte[] foto_tomada, String UbicacionFoto, String ComentarioFoto) throws Exception {
        List<ListadoFoto> CargarFotos =  null;
        try{
            String SelectList = "SELECT * FROM foto_listado WHERE ID_FOTO_TOMADA = ? AND ID_UBICACION = ? AND ID_COMENTARIO = ?";
            String [] SelectionArgs = new String[]{String.valueOf(id_fotos), String.valueOf(foto_tomada),UbicacionFoto,ComentarioFoto};
            Cursor Curseado = DbFotos.rawQuery(SelectList,SelectionArgs);
            CargarFotos = new ArrayList<>();
            if(Curseado.moveToFirst()){
                while(!Curseado.isAfterLast()){
                    ListadoFoto ItemSeleccionado = new ListadoFoto(id_fotos,foto_tomada,UbicacionFoto,ComentarioFoto);
                    ItemSeleccionado.setID_foto(Curseado.getInt(Curseado.getColumnIndexOrThrow("ID_FOTO")));
                    ItemSeleccionado.setFoto_Tomada(Curseado.getString(Curseado.getColumnIndexOrThrow("ID_FOTO_TOMADA")));
                    ItemSeleccionado.setUbicacionFoto(Curseado.getString(Curseado.getColumnIndexOrThrow("ID_UBICACION")));
                    ItemSeleccionado.setComentario(Curseado.getString(Curseado.getColumnIndexOrThrow("ID_COMENTARIO")));
                    CargarFotos.add(ItemSeleccionado);
                }
            }
            Curseado.close();
        }catch(Exception ER){
            throw new Exception(ER.getMessage());
        }
        return CargarFotos;
    }


}
