package com.example.fotoubicacion.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fotoubicacion.Entidades.FotosModels;
import com.example.fotoubicacion.Models.Fotos;

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

    public void InsertarFotoDB(Fotos fotos) throws Exception{
        int Ultimo_id = 0;

        try{
            String Solicitud = "INSERT INTO foto_listado ( " +
                    "ID_FOTO_TOMADA, " +
                    "IMAGEN_REAL, " +
                    "ID_UBICACION, " +
                    "ID_COMENTARIO) " +
                    "VALUES (?,?,?,?)";
            DbFotos = DBHelp.getWritableDatabase();
            DbFotos.execSQL(Solicitud, new Object[]{
                    fotos.getPathImagen(),
                    fotos.getImagenReal(),
                    fotos.getUbicacionSolicitada(),
                    fotos.getComentarioFoto()}
            );

            DBHelp.close();
        }catch (Exception MensajeERROR){
            throw new Exception(MensajeERROR.getMessage());
        }
    }

    public List<FotosModels> CargarListadoFoto(int id_fotos, String foto_tomada, byte[] imagen_real, String UbicacionFoto, String ComentarioFoto) throws Exception {
        List<FotosModels> CargarFotos =  null;
        try{
            String SelectList = "SELECT * FROM foto_listado WHERE ID_FOTO_TOMADA = ? AND ID_UBICACION = ? AND ID_COMENTARIO = ?";
            String [] SelectionArgs = new String[]{String.valueOf(id_fotos), String.valueOf(foto_tomada),UbicacionFoto,ComentarioFoto};
            Cursor Curseado = DbFotos.rawQuery(SelectList,SelectionArgs);
            CargarFotos = new ArrayList<>();
            if(Curseado.moveToFirst()){
                while(!Curseado.isAfterLast()){
                    FotosModels ItemSeleccionado = new FotosModels(id_fotos,foto_tomada,imagen_real,UbicacionFoto,ComentarioFoto);
                    ItemSeleccionado.setID_foto(Curseado.getInt(Curseado.getColumnIndexOrThrow("ID_FOTO")));
                    ItemSeleccionado.setPath_Foto(Curseado.getString(Curseado.getColumnIndexOrThrow("FOTO_TOMADA")));
                    ItemSeleccionado.setImagen_Real(Curseado.getBlob(Curseado.getColumnIndexOrThrow("IMAGEN_REAL")));
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
            dbfotoelegir.execSQL("DELETE FROM foto_listado WHERE ID_FOTO ="+String.valueOf(id));
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
                    fotosDp.setPathImagen(cursor.getString(cursor.getColumnIndexOrThrow("ID_FOTO_TOMADA")));
                    fotosDp.setImagenReal(cursor.getBlob(cursor.getColumnIndexOrThrow("IMAGEN_REAL")));
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

    public boolean ModificarComentarios(int id,String ubicacion,String comentario) {
        boolean seleccion = false;
        SQLiteDatabase sql = DBHelp.getWritableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put("ID_UBICACION",ubicacion); //These Fields should be your String values of actual column names
            cv.put("ID_COMENTARIO",comentario);
            sql.update("foto_listado", cv, "ID_FOTO = ?",new String[]{ String.valueOf(id)});
            seleccion = true;

        }catch (Exception Error){
            Error.toString();
        }finally {
            DBHelp.close();
        }
        return seleccion;
    }

}
