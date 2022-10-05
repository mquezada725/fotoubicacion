package com.example.fotoubicacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fotoubicacion.DB.DBHelper;
import com.example.fotoubicacion.DB.FormularioDB;
import com.example.fotoubicacion.DB.FotosDB;
import com.example.fotoubicacion.Models.Formulario;
import com.example.fotoubicacion.Models.Fotos;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //Tipo de botones en el menu principal
    Button PasarFotos, PasarEditar,PasarFormulario,CrearDBnuevoRecorrido,Sincronizar;

    RequestQueue requestQueue;

    //Llamada de clase de formulario java class
    FormularioDB dbform;

    //Llamada de clase de fotos java class
    FotosDB dbfotos;

    //Llamada de clase tools java class
    Tools tools;

    //llamada de clase de Base de datos local
    private DBHelper DBHelp;

    private static final String URL2 = "http://192.168.0.167:50326/";
    private static final String WEBURL = "http://captel_api.openpanel.cl/swagger/ui/index";
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.fondopantalla);
        getSupportActionBar().hide();

        //llamamos las instancia de fotos y formulario de base de datos locales
        dbform = new FormularioDB(MainActivity.this);
        dbfotos = new FotosDB(MainActivity.this);
        tools = new Tools(MainActivity.this);


        //Asociacion de botones fisico con tipo de formato definido
        PasarFotos = findViewById(R.id.BtnNuevaFoto);
        PasarFormulario = findViewById(R.id.BtnFormulario);
        PasarEditar = findViewById(R.id.BtnEditarListado);
        CrearDBnuevoRecorrido = findViewById(R.id.BtnRecorridoNuevo);
        Sincronizar = findViewById(R.id.btnSincronizacion);

        requestQueue = Volley.newRequestQueue(this);

        Sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder Build = new AlertDialog.Builder(MainActivity.this);
                Build.setMessage("Desea Enviar Datos A Base de Datos? \n" +
                                "!Asegurese de generar un formulario! ")
                        .setTitle("Confirmación");
                Build.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PublicarPost();
                        Toast.makeText(MainActivity.this, "Datos Enviado a servidor", Toast.LENGTH_SHORT).show();
                    }
                });
                Build.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Datos No Enviado al servidor", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = Build.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.LTGRAY);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.LTGRAY);
            }
        });

        PasarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IntentoFoto = new Intent(MainActivity.this, GenerarFotos.class);
                startActivity(IntentoFoto);
            }

        });

        PasarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IntentoFormulario = new Intent(MainActivity.this, FormularioRellenar.class);
                startActivity(IntentoFormulario);
            }
        });


        PasarEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IntentoEditar = new Intent(MainActivity.this, EditarRecorrido.class);
                startActivity(IntentoEditar);
            }
        });

        CrearDBnuevoRecorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Desea Generar Nuevo Recorrido ?")
                        .setTitle("Confirmación");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Eliminardatos();
                        Toast.makeText(MainActivity.this, "Recorrido Nuevo Creado", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Recorrido No Creado", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.LTGRAY);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.LTGRAY);
            }
        });
    }

    // metodos para los posteos de fotos y formulario unico
    //debe encontrarse con interntet para completarse a la pagina
    //
    private void PublicarFoto(int registro, int item, Fotos fotosSel)  {

        JSONObject object = new JSONObject();

        try{
            //String b64= Base64.encodeToString(tools.decodeByte(fotosSel.getImagenTomada()), Base64.DEFAULT);

            object.put("fotos", Base64.encodeToString(tools.decodeByte(fotosSel.getImagenTomada()), Base64.DEFAULT));
            object.put("Ubicacion" , fotosSel.getUbicacionSolicitada());
            object.put("observacion" , fotosSel.getComentarioFoto());
            object.put("registro" , registro);
            object.put("item" , item);

            file = new File(fotosSel.getImagenTomada());
            object.put("nombre" , file.getName());
            //if (file.getName().contains("."))
            //file.getName().substring(file.getName().lastIndexOf("."));
            object.put("extension" ,  file.getName().substring(file.getName().lastIndexOf(".")));

        }catch (JSONException ER){
            ER.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WEBURL + "/api/Formulario/postfotos", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MainActivity.this, " Foto Publicada : " + response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    Log.d("JSON", String.valueOf(response));
                    String Falla = response.getString("httpStatus");
                    if (Falla.equals("") || Falla.equals(null)) {

                    } else if (Falla.equals("OK")) {
                        JSONObject body = response.getJSONObject("body");

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error :" + error.getMessage());
                Toast.makeText(MainActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void PublicarPost(){

        Formulario form = new Formulario();
        try {
            form = dbform.SelectFormulario();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject object = new JSONObject();
        try {
            object.put("Titulo_Form", form.getTitulo_Form());
            object.put("Fecha_Form" , form.getFecha_Form());
            object.put("Numero_Evento", form.getNumero_Evento());
            object.put("Supervisor_Turno", form.getSupervisor_Turno());
            object.put("Tecnico_Reparacion" , form.getTecnico_Reparacion());
            object.put("Tecnico_ReparacionOP" , form.getTecnico_ReparacionOP());
            object.put("Zona_Mantenimiento", form.getZona_Mantenimiento());
            object.put("Fecha_Inicio" , form.getFecha_Inicio());
            object.put("Fecha_Termino", form.getFecha_Termino());
            object.put("Tiempo_Total_Actividad" , form.getTiempo_Total_Actividad());
            object.put("Cliente_Afectado", form.getCliente_Afectado());
            object.put("Tipo_Cliente", form.getTipo_Cliente());
            object.put("Localizacion_Falla", form.getLocalizacion_Falla());
            object.put("Cerrar_Modificacion" , form.getCerrar_Modificacion());
            object.put("Descripcion_Materiales", form.getDescripcion_Materiales());
            object.put("Descripcion_Trabajo" , form.getDescripcion_Trabajo());
            object.put("Resolucion_Tabajo", form.getResolucion_Tabajo());
            object.put("Observaciones" , form.getObservaciones());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WEBURL+"/api/Formulario/postform", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(MainActivity.this, "String Respuesta Form : " + response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            //Log.d("JSON", String.valueOf(response));
                            //String Falla = response.getString("httpStatus");
                            int registro = Integer.parseInt(response.getString("registro"));
                            //Fotos fotosSel = new Fotos();
                            try{
                                int item = 0;
                                for(Fotos fotosSel: dbfotos.FotoPrueba()){
                                    item++;
                                    PublicarFoto(registro, item, fotosSel);
                                }
                                //fotosSel = dbfotos.FotoPrueba();
                            }catch (Exception er){
                                er.printStackTrace();
                            }
                            /*
                            if (Falla.equals("") || Falla.equals(null)) {

                            } else if (Falla.equals("OK")) {
                                JSONObject body = response.getJSONObject("body");
                            } else {
                            }
                            */
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error :" + error.getMessage());
                Toast.makeText(MainActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void Eliminardatos(){
        Formulario form = new Formulario();
        Fotos fotform = new Fotos();
        try {

            dbform.deleteformulario();
            dbfotos.deleteFoto();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}

//object.put("Distancia_Optica", form.getDistancia_Optica());
//object.put("Latitud_Dato", form.getLatitud_Dato());
//object.put("Longitud_Dato",form.getLongitud_Dato());
//object.put("Registro Foto Antes", R.id.RgFotoAntes);
//object.put("Registro Foto al Final", R.id.RgFotoFinal);
//object.put("Registro_Medicion_Antes", form.getRegistro_Medicion_Antes());
//object.put("Registro_Medicion_Final", form.getRegistro_Medicion_Final());
//object.put("Perdida_Servicio", form.getPerdida_Servicio());
//object.put("Asociado_Evento" , form.getAsociado_Evento());
