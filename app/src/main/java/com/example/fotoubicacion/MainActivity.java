package com.example.fotoubicacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fotoubicacion.DB.DBHelper;
import com.example.fotoubicacion.DB.FormularioDB;
import com.example.fotoubicacion.DB.FotosDB;
import com.example.fotoubicacion.Models.Bodegas;
import com.example.fotoubicacion.Models.Clientes;
import com.example.fotoubicacion.Models.Datosform;
import com.example.fotoubicacion.Models.Formulario;
import com.example.fotoubicacion.Models.Fotos;
import com.example.fotoubicacion.Models.Operarios;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static Context context;

    public static void setContext(Context cntxt) {
        context = cntxt;
    }

    public static Context getContext() {
        return context;
    }

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    //Tipo de botones en el menu principal
    Button TomarFotos, EditarFormulario, LlenarFormulario, BorrarFormulario, SubirFormulario, SincronizarTablas;
    TextView TxtEmpresa;

    RequestQueue requestQueue, requestQueuefotos;

    //Llamada de clase de formulario java class
    FormularioDB dbform;

    //Llamada de clase de fotos java class
    FotosDB dbfotos;

    //Llamada de clase tools java class
    Tools tools;

    //llamada de clase de Base de datos local
    private DBHelper DBHelp;

    private int registro;

   // private static final String URL2 = "http://192.168.1.102:50330/";
    private static final String URL2 = "http://captel_api.openpanel.cl/";
    //private static final String URL2 = "http://172.20.10.2:50330/";
    private File file; String tag; String message; Throwable exception;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.fondopantalla);
        getSupportActionBar().hide();
        this.setContext(getApplicationContext());

        //llamamos las instancia de fotos y formulario de base de datos locales
        dbform = new FormularioDB(MainActivity.this);
        dbfotos = new FotosDB(MainActivity.this);
        tools = new Tools(MainActivity.this);

        //Asociacion de botones fisico con tipo de formato definido
        TomarFotos = findViewById(R.id.BtnTomarFotos);
        TomarFotos.setBackgroundColor(Color.WHITE);
        TomarFotos.setTextColor(Color.BLACK);

        LlenarFormulario = findViewById(R.id.BtnLlenarFormulario);
        LlenarFormulario.setBackgroundColor(Color.WHITE);
        LlenarFormulario.setTextColor(Color.BLACK);

        EditarFormulario = findViewById(R.id.BtnEditarFotos);
        EditarFormulario.setBackgroundColor(Color.WHITE);
        EditarFormulario.setTextColor(Color.BLACK);

        BorrarFormulario = findViewById(R.id.BtnBorrarFormulario);
        BorrarFormulario.setBackgroundColor(Color.WHITE);
        BorrarFormulario.setTextColor(Color.BLACK);

        SubirFormulario = findViewById(R.id.btnSubirFormulario);
        SubirFormulario.setBackgroundColor(Color.WHITE);
        SubirFormulario.setTextColor(Color.BLACK);

        SincronizarTablas = findViewById(R.id.btnSincronizarTablas);
        SincronizarTablas.setBackgroundColor(Color.WHITE);
        SincronizarTablas.setTextColor(Color.BLACK);

        requestQueue = Volley.newRequestQueue(this);
        requestQueuefotos = Volley.newRequestQueue(this);

        SubirFormulario.setOnClickListener(new View.OnClickListener() {
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

        TomarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IntentoFoto = new Intent(MainActivity.this, GenerarFotos.class);
                startActivity(IntentoFoto);
            }

        });

        LlenarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IntentoFormulario = new Intent(MainActivity.this, FormularioRellenar.class);
                startActivity(IntentoFormulario);
            }
        });

        EditarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IntentoEditar = new Intent(MainActivity.this, EditarRecorrido.class);
                startActivity(IntentoEditar);
            }
        });

        BorrarFormulario.setOnClickListener(new View.OnClickListener() {
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

        SincronizarTablas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sincronizar_Tablas();
            }
        });


    }
    private Datosform datosform;
    private ArrayList<Operarios> listop = new ArrayList<>();
    private ArrayList<Bodegas> listzo = new ArrayList<>();
    private ArrayList<Clientes> listcl = new ArrayList<>();

    public void Sincronizar_Tablas(){
        Log.i("INFO", "ACTUALIZA TABLAS:"+URL2);
        JsonObjectRequest lectortablas = new JsonObjectRequest(
            Request.Method.GET,
            URL2 + "api/Formulario/datosform",
            null,
            new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("INFO", "LEE OPERARIOS:");
                    JSONArray j = response.getJSONArray("Operarios");
                    for (int i = 0; i < j.length(); i++) {
                        try {
                            Operarios op = new Operarios();
                            JSONObject obj = j.getJSONObject(i);
                            op.setRut(obj.getInt("rut"));
                            op.setNombre(obj.getString("nombre"));
                            op.setTipo(obj.getString("tipo"));
                            listop.add(op);
                        } catch (JSONException e) {
                            Log.i("INFO", "ERROR OPERARIOS:"+URL2);
                            e.printStackTrace();
                        }
                    }
                    Guardar_Preferencias("Operarios",  listop);
                    //
                    //  Zonas
                    //
                    JSONArray jzo = response.getJSONArray("Bodegas");
                    for (int i = 0; i < jzo.length(); i++) {
                        try {
                            Bodegas zo = new Bodegas();
                            JSONObject obj = jzo.getJSONObject(i);
                            zo.setCodigo(obj.getInt("codigo"));
                            zo.setNombre(obj.getString("nombre"));
                            listzo.add(zo);
                        } catch (JSONException e) {
                            Log.i("INFO", "ERROR ZONAS:"+URL2);
                            e.printStackTrace();
                        }
                    }
                    Guardar_Preferencias("Zonas",  listzo);
                    //
                    //  Clientes
                    //
                    JSONArray jcl = response.getJSONArray("Clientes");
                    for (int i = 0; i < jcl.length(); i++) {
                        try {
                            Clientes cl = new Clientes();
                            JSONObject obj = jcl.getJSONObject(i);
                            cl.setReferencia(obj.getInt("referencia"));
                            cl.setNombre(obj.getString("nombre"));
                            listcl.add(cl);
                        } catch (JSONException e) {
                            Log.i("INFO", "ERROR CLIENTES:"+URL2);
                            e.printStackTrace();
                        }
                    }
                    Guardar_Preferencias("Clientes",  listcl);
                    Toast.makeText(context, "Tablas actualizadas !!", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "+++ ERROR DE CONEXION !!", Toast.LENGTH_SHORT).show();
                }
              }
            },
            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        Log.i("INFO", "ACTUALIZA TABLAS:"+res);

                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                // For AuthFailure, you can re login with user credentials.
                // In this case you can check how client is forming the api and debug accordingly.
                // For ServerError 5xx, you can do retry or handle accordingly.
                if( error instanceof NetworkError) {
                    Log.i("INFO","network error");
                } else if( error instanceof ServerError) {
                    Log.i("INFO","Servor error status code5**");
                } else if( error instanceof AuthFailureError) {
                    Log.i("INFO","credenciales invalidas");
                } else if( error instanceof ParseError) {
                    Log.i("INFO","no se pudo parsear datos");
                } else if( error instanceof NoConnectionError) {
                    Log.i("INFO","no hay conexion");
                } else if( error instanceof TimeoutError) {
                    Log.i("INFO","timeout error");
                }
                Toast.makeText(context, "+++ ERROR DE CONEXION !!", Toast.LENGTH_SHORT).show();

            }
        });
        lectortablas.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(lectortablas);
    }

    private void Guardar_Preferencias(String contenido, ArrayList lista) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Gson gson = new GsonBuilder()
        //        .excludeFieldsWithoutExposeAnnotation()
        //        .setPrettyPrinting()
        //        .create();
        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(lista);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString(contenido, json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.commit();
        Log.i("INFO", "PREFERENCIAS GUARDADAS");

        // after saving data we are displaying a toast message.
    }

    private void PublicarPost(){
        Formulario form = new Formulario();
        try {
            form = dbform.SelectFormulario();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int id=form.getId();
        JSONObject object = new JSONObject();
        try {
            object.put("registro", form.getRegistro());
            object.put("Titulo_Form", form.getTitulo_Form());
            object.put("Fecha_Form" , form.getFecha_Form());
            object.put("Supervisor_Turno", form.getSupervisor_Turno());
            object.put("Tecnico_Reparacion" , form.getTecnico_Reparacion());
            object.put("Tecnico_ReparacionOP" , form.getTecnico_ReparacionOP());
            object.put("Zona_Mantenimiento", form.getZona_Mantenimiento());
            object.put("Fecha_Inicio" , form.getFecha_Inicio());
            object.put("Fecha_Termino", form.getFecha_Termino());
            object.put("Tiempo_Total_Actividad" , form.getTiempo_Total_Actividad());
            object.put("Cliente_Afectado", form.getCliente_Afectado());
            object.put("Referencia_Cliente", form.getReferencia_Cliente());
            String tipocl=String.valueOf(form.getTipo_Cliente());
            if (tipocl.equals("Troncal")){
            object.put("Tipo_Cliente", "1");
            }
            else{
                object.put("Tipo_Cliente", "2");
            }
            object.put("Localizacion_Falla", form.getLocalizacion_Falla());
            object.put("Descripcion_Materiales", form.getDescripcion_Materiales());
            object.put("Descripcion_Trabajo" , form.getDescripcion_Trabajo());
            object.put("Resolucion_Trabajo", form.getResolucion_Tabajo());
            object.put("Observaciones" , form.getObservaciones());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL2+"api/Formulario/postform",
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("INFO", "INICIANDO SUBIDA FORMULARIO");
                        try {
                            //Log.d("JSON", String.valueOf(response));
                            //String Falla = response.getString("httpStatus");
                            registro = Integer.parseInt(response.getString("registro"));
                            try {
                                dbform.UpdateRegistroFormulario(id,registro);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.i("INFO", "REGISTRO "+ String.valueOf(registro));
                            try{
                                int item = 0;
                                for(Fotos fotosSel: dbfotos.FotoPrueba()){
                                    item++;
                                    PublicarFoto(registro, item, fotosSel);
                                    Log.i("INFO", "SUBIDA FOTO "+item+" EXITOSA CON REGISTRO :"+ String.valueOf(registro));
                                }
                                Toast.makeText(MainActivity.this, "Formulario Enviado", Toast.LENGTH_SHORT).show();

                            }catch (Exception er){
                                er.printStackTrace();
                                Log.i("INFO", "SUBIDA FORMULARIO NO PUDO HACERSE :"+ String.valueOf(registro));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("INFO", "ERROR AL SUBIR FORMULARIO");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                Log.i("INFO", "SUBIENDO FORMULARIO:"+res);

                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                        Toast.makeText(MainActivity.this, "Error al Enviar", Toast.LENGTH_SHORT).show();

                        // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                        // For AuthFailure, you can re login with user credentials.
                        // In this case you can check how client is forming the api and debug accordingly.
                        // For ServerError 5xx, you can do retry or handle accordingly.
                        if( error instanceof NetworkError) {
                            Log.i("INFO","network error");
                        } else if( error instanceof ServerError) {
                            Log.i("INFO","Servidor error status code5**");
                        } else if( error instanceof AuthFailureError) {
                            Log.i("INFO","credenciales invalidas");
                        } else if( error instanceof ParseError) {
                            Log.i("INFO","no se pudo parsear datos");
                        } else if( error instanceof NoConnectionError) {
                            Log.i("INFO","no hay conexion");
                        } else if( error instanceof TimeoutError) {
                            Log.i("INFO","timeout error");
                        }                    }

                });

        myRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(myRequest);
    }

    private void PublicarFoto(int registro, int item, Fotos fotosSel)  {

        JSONObject object = new JSONObject();

        try{

            byte[] fileByteArray = tools.decodeByte(fotosSel.getPathImagen());
            Bitmap bitmap = BitmapFactory.decodeByteArray(fileByteArray, 0, fileByteArray.length);
            Bitmap resizedBitmap = getResizedBitmap(bitmap, 1000);
            ByteArrayOutputStream baOutStream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baOutStream);
            byte[] byteArray = baOutStream.toByteArray();

            object.put("fotos", Base64.encodeToString(byteArray, Base64.DEFAULT));
          //  object.put("fotos", Base64.encodeToString(fileByteArray, Base64.DEFAULT));
            object.put("Ubicacion" , fotosSel.getUbicacionSolicitada());
            object.put("observacion" , fotosSel.getComentarioFoto());
            object.put("registro" , registro);
            object.put("item" , item);
            file = new File(fotosSel.getPathImagen());
            object.put("nombre" , file.getName());
            object.put("extension" ,  file.getName().substring(file.getName().lastIndexOf(".")));

        }catch (JSONException ER){
            ER.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL2 + "/api/Formulario/postfotos", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("INFO", "SUBIENDO FOTO......");
                try {
                    Log.d("JSON", String.valueOf(response));
                    String Falla = response.getString("httpStatus");
                    if (Falla.equals("") || Falla.equals(null)) {
                        Log.i("INFO", "FOTO SUBIDA OK ......");
                    } else if (Falla.equals("OK")) {
                        JSONObject body = response.getJSONObject("body");
                    } else {
                        Log.i("INFO", "FOTO : "+Falla);
                    }
                } catch (JSONException e) {
                    Log.i("INFO", "FOTO FALLA ");
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

//        RequestQueue requestQueuefotosxxx = Volley.newRequestQueue(getApplicationContext());
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueuefotos.add(jsonObjectRequest);
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

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}

