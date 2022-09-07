package com.example.fotoubicacion.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;

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


    //Metodo para crear INSERT
    public void InsercionFormulario(String Titulo_Formulario, String Fecha, String RGEvento, String SupervisorSel, String TecnicoRep, String TecnicoRep2, String ZonaMant, String FechaHoraIni,
    String FechaHoraTer, String FechatotalAct, String ClienteAfec,String TipCliente, String LocFalla, String DistOptica, String Latit, String Longt,
    String RegFotoAntes,String RegFotoFinal, String RegMedAntes, String RegMedFinal, String CerrarMod,
    String DescripMaterial, String DescripTrabajo, String ResolTrabajo, String Obser) throws Exception{

        try{
            String Solicitud = "INSERT INTO formulario_recorrido (" + "" +
                    "TITULO_FORMULARIO," +
                    "FECHA_FORMULARIO, " +
                    "ASOCIADOEVENTO," +
                    "SUPERVISORTURNO," +
                    "TECNICOREPARACION," +
                    "TECNICOREPARACION2," +
                    "ZONAMANTENIMIENTO," +
                    "FECHAINICIOHORAACT," +
                    "FECHATERMINOHORAACT," +
                    "FECHATOTAL," +
                    "CLIENTEAFECTADO," +
                    "TIPOCLIENTE," +
                    "LOCALIZACIONFALLA," +
                    "DISTANCIAOPTICA," +
                    "LATITUD," +
                    "LONGITUD," +
                    "REGISTROFOTOANTES," +
                    "REGISTROFOTOFINAL," +
                    "REGISTROMEDICIONANTES," +
                    "REGISTROMEDICIONFINAL," +
                    "CERRARMODIFICACION," +
                    "DESCRIPCIONMATERIAL," +
                    "DESCRIPCIONTRABAJO," +
                    "RESOLUCIONTRABAJO," +
                    "OBSERVACION) VALUES (" + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            DB.execSQL(Solicitud, new Object[]{
                            Titulo_Formulario,
                            Fecha,
                            RGEvento,
                            SupervisorSel,
                            TecnicoRep,
                            TecnicoRep2,
                            ZonaMant,
                            FechaHoraIni,
                            FechaHoraTer,
                            FechatotalAct,
                            ClienteAfec,
                            TipCliente,
                            LocFalla,
                            DistOptica,
                            Latit,
                            Longt,
                            RegFotoAntes,
                            RegFotoFinal,
                            RegMedAntes,
                            RegMedFinal,
                            CerrarMod,
                            DescripMaterial,
                            DescripTrabajo,
                            ResolTrabajo,
                            Obser}
                    );

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
}
