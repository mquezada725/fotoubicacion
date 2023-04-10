package com.example.fotoubicacion.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.fotoubicacion.Models.Formulario;

import java.text.Normalizer;

public class FormularioDB {

    private SQLiteDatabase DB;
    private DBHelper SQL;

    public FormularioDB(Context ContenidoFor){
        SQL = new DBHelper(ContenidoFor);
    }

    public void apertura(){
        DB = SQL.getWritableDatabase();
    }

    public void Cerrar(){
        SQL.close();
    }

    public Formulario SelectFormulario() throws Exception {
        Formulario form = new Formulario();
        try{
            DB = SQL.getWritableDatabase();
            String query = "SELECT * FROM Formulario_recorrido";
            String[] selectionsArgs = new String[]{};
            Cursor cursor = DB.rawQuery(query, selectionsArgs);
            if (cursor != null && cursor.moveToFirst()) {
                form.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID_FORMULARIO")));
                form.setRegistro(cursor.getInt(cursor.getColumnIndexOrThrow("REGISTRO")));
                form.setTitulo_Form(cursor.getString(cursor.getColumnIndexOrThrow("TITULO_FORMULARIO")));
                form.setFecha_Form(cursor.getString(cursor.getColumnIndexOrThrow("FECHA_FORMULARIO")));
                form.setSupervisor_Turno(cursor.getString(cursor.getColumnIndexOrThrow("SUPERVISORTURNO")));
                form.setTecnico_Reparacion(cursor.getString(cursor.getColumnIndexOrThrow("TECNICOREPARACION")));
                form.setTecnico_ReparacionOP(cursor.getString(cursor.getColumnIndexOrThrow("TECNICOREPARACION2")));
                form.setZona_Mantenimiento(cursor.getString(cursor.getColumnIndexOrThrow("ZONAMANTENIMIENTO")));
                form.setFecha_Inicio(cursor.getString(cursor.getColumnIndexOrThrow("FECHAINICIOHORAACT")));
                form.setFecha_Termino(cursor.getString(cursor.getColumnIndexOrThrow("FECHATERMINOHORAACT")));
                form.setTiempo_Total_Actividad(cursor.getString(cursor.getColumnIndexOrThrow("FECHATOTAL")));
                form.setCliente_Afectado(cursor.getString(cursor.getColumnIndexOrThrow("CLIENTEAFECTADO")));
                form.setReferencia_Cliente(cursor.getString(cursor.getColumnIndexOrThrow("REFERENCIACLIENTE")));
                form.setTipo_Cliente(cursor.getString(cursor.getColumnIndexOrThrow("TIPOCLIENTE")));
                form.setLocalizacion_Falla(cursor.getString(cursor.getColumnIndexOrThrow("LOCALIZACIONFALLA")));
                form.setDescripcion_Materiales(cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCIONMATERIAL")));
                form.setDescripcion_Trabajo(cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCIONTRABAJO")));
                form.setResolucion_Tabajo(cursor.getString(cursor.getColumnIndexOrThrow("RESOLUCIONTRABAJO")));
                form.setObservaciones(cursor.getString(cursor.getColumnIndexOrThrow("OBSERVACION")));
            }

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        DB.close();
        return form;
    }

    public void InsercionFormulario( Formulario form )throws Exception{
        try{
            String Solicitud = "INSERT INTO formulario_recorrido (" +
                    "REGISTRO ," +
                    "TITULO_FORMULARIO," +
                    "FECHA_FORMULARIO, " +
                    "SUPERVISORTURNO," +
                    "TECNICOREPARACION," +
                    "TECNICOREPARACION2," +
                    "ZONAMANTENIMIENTO," +
                    "FECHAINICIOHORAACT," +
                    "FECHATERMINOHORAACT," +
                    "FECHATOTAL," +
                    "CLIENTEAFECTADO," +
                    "REFERENCIACLIENTE," +
                    "TIPOCLIENTE," +
                    "LOCALIZACIONFALLA," +
                    "DESCRIPCIONMATERIAL," +
                    "DESCRIPCIONTRABAJO," +
                    "RESOLUCIONTRABAJO," +
                    "OBSERVACION) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            DB = SQL.getWritableDatabase();
            String tipocliente=form.getTipo_Cliente();
            DB.execSQL(Solicitud, new Object[]{
                            form.getRegistro(),
                            form.getTitulo_Form(),
                            form.getFecha_Form(),
                            form.getSupervisor_Turno(),
                            form.getTecnico_Reparacion(),
                            form.getTecnico_ReparacionOP(),
                            form.getZona_Mantenimiento(),
                            form.getFecha_Inicio(),
                            form.getFecha_Termino(),
                            form.getTiempo_Total_Actividad(),
                            form.getCliente_Afectado(),
                            form.getReferencia_Cliente(),
                            form.getTipo_Cliente(),
                            form.getLocalizacion_Falla(),
                            form.getDescripcion_Materiales(),
                            form.getDescripcion_Trabajo(),
                            form.getResolucion_Tabajo(),
                            form.getObservaciones()}
                    );
            //SQL.close();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    public void UpdateRegistroFormulario( int id,int registro )throws Exception{
        try{
            DB = SQL.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("REGISTRO",String.valueOf(registro));
            DB.update("formulario_recorrido", cv, "ID_FORMULARIO = ?", new String[]{String.valueOf(id)});
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    //METODO DE ELIMINACION DE COLUMNAS DE TABLA FORMULARIO

    public void deleteformulario() throws Exception {

        try{

            String query = "DELETE FROM formulario_recorrido ";

            DB = SQL.getWritableDatabase();
            //DB.execSQL(query,  new Object[]{query, new Object[]{} });
            DB.execSQL(query);


        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

}
