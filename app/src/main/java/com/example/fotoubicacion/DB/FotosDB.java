package com.example.fotoubicacion.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import com.example.fotoubicacion.Entidades.ListadoFoto;
import com.example.fotoubicacion.Models.Fotos;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FotosDB {
    private SQLiteDatabase DbFotos;
    private DBHelper DBHelp;
    private ByteArrayOutputStream byteArrayOutputStream;


    public FotosDB(Context Contenido){
            DBHelp = new DBHelper(Contenido);
    }


    public void Abertura(){
        DbFotos = DBHelp.getWritableDatabase();
    }

    public void Clausura(){
        DBHelp.close();
    }

    public void InsertarFotoDB(Fotos fotos) throws Exception{
        int Ultimo_id = 0;

        try{

            String Solicitud = "INSERT INTO foto_listado ( " +
                    "ID_FOTO_TOMADA, " +
                    "ID_UBICACION, " +
                    "ID_COMENTARIO) " +
                    "VALUES (?,?,?)";
            DbFotos = DBHelp.getWritableDatabase();
            DbFotos.execSQL(Solicitud, new Object[]{
                    fotos.getImagenTomada(),
                    fotos.getUbicacionSolicitada(),
                    fotos.getComentarioFoto()}
            );

            DBHelp.close();
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
                    ItemSeleccionado.setFoto_Tomada(Curseado.getBlob(Curseado.getColumnIndexOrThrow("FOTO_TOMADA")));
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

    public boolean EliminarFoto(int id){
            boolean seleccion = false;
            SQLiteDatabase dbfotoelegir = DBHelp.getWritableDatabase();
        try{
            dbfotoelegir.execSQL("DELETE FROM foto_listado WHERE ID_FOTO_TOMADA = ?");
            seleccion = true;

        }catch (Exception Error){
            Error.toString();
        }finally {
            DBHelp.close();
        }
        return seleccion;
    }

    public List<Fotos> FotoPrueba() throws Exception {

        List<Fotos> fotoslist = new ArrayList<>();
        try {
            DbFotos = DBHelp.getWritableDatabase();
            String query = "SELECT * FROM foto_listado";
            String[] selectionsArgs = new String[]{};
            Cursor cursor = DbFotos.rawQuery(query, selectionsArgs);

            if(cursor != null && cursor.moveToFirst()){
                while (!cursor.isAfterLast()) {
                    Fotos fotosDp = new Fotos();
                    fotosDp.setImagenTomada(cursor.getString(cursor.getColumnIndexOrThrow("ID_FOTO_TOMADA")));
                    fotosDp.setUbicacionSolicitada(cursor.getString(cursor.getColumnIndexOrThrow("ID_UBICACION")));
                    fotosDp.setComentarioFoto(cursor.getString(cursor.getColumnIndexOrThrow("ID_COMENTARIO")));
                    cursor.moveToNext();
                    fotoslist.add(fotosDp);
                }
            }
            cursor.close();

        } catch (Exception er) {
            throw new Exception(er.getMessage());
        }
        //DbFotos.close();
        return fotoslist;
    }

    public void deleteFoto() throws Exception {

        try{
            String query = "DELETE FROM foto_listado ";
            DbFotos = DBHelp.getWritableDatabase();
            DbFotos.execSQL(query);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }


}
