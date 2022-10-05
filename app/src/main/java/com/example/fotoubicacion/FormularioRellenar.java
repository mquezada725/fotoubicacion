package com.example.fotoubicacion;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fotoubicacion.DB.DBHelper;
import com.example.fotoubicacion.DB.FormularioDB;
import com.example.fotoubicacion.Models.Bodegas;
import com.example.fotoubicacion.Models.Datosform;
import com.example.fotoubicacion.Models.Formulario;
import com.example.fotoubicacion.Models.Operarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.AccessControlContext;
import java.text.ParseException;
import java.time.Duration;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

public class FormularioRellenar extends AppCompatActivity {


    //Url Conectado para envio de datos a servidor
    private static final String URL2 = "http://192.168.0.167:50326/";
    private static final String URLWEB = "http://captel_api.openpanel.cl/swagger/ui/index";


    private RequestQueue queue;


    //Establecemos Comunicacion Con Sqlite
    SQLiteDatabase sqLiteDb;

    //
    FormularioDB dbform;
    DBHelper DB;


    //Definicion de tipos datos entrantes
    EditText TituloRecorrido, FechaRecorrido, LocalizacionFalla, DescribirTrabajo, ResolucionTrabajo,
            ObservacionTrabajo, DescribirMateriales, Eventos, Latitud, Longitud, FechaHoraInicioActividad,
            FechaHoraTerminoActividad, TiempoTotalActividad;


    RadioButton TipoClienteTroncal, TipoClienteUltimaMilla, CerrarModificacionSi, CerrarModificacionNo;

    RadioGroup GrupoTipoCliente, GrupoCerrarModificacion;

    Spinner SupervisorTurno, TecnicoReparacion, TecnicoReparacion2, ZonaMantenimiento, ClienteAfectado;

    Button GuardarRecorrido;
    AlertDialog alertDialog;


    //-Definir tipo de datos para adaptar fecha y hora
    int Hora, Minutos, HoraT, MinutosT, HoraCompleto, MinutosCompleto;
    int Dia, Mes, Years, DiaT, MesT, YearsT, DiaI, MesI, YearsI;
    int mHour, mMinute, year, month, day;


    //Metodo para evento Volley
    private String rutSup = "", rutTecRep1 = "", rutTecRep2 = "";
    private String Zonaman = "";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_rellenar);
        getSupportActionBar().hide();
        //Metodo de base de dato y Tipo Volley
        dbform = new FormularioDB(FormularioRellenar.this);
        queue = Volley.newRequestQueue(FormularioRellenar.this);


        //Establecemos Comunicacion Con los datos En la Parte Grafica

        // Tipo EditText
        TituloRecorrido = findViewById(R.id.TxtTitulo);
        FechaRecorrido = findViewById(R.id.TxtFechaObtenido);
        LocalizacionFalla = findViewById(R.id.TxtLocalizacionFalla);
        DescribirMateriales = findViewById(R.id.TxtDescribirMateriales);
        DescribirTrabajo = findViewById(R.id.TxtDescribirTrabajo);
        ResolucionTrabajo = findViewById(R.id.TxtResolutivoTrabajo);
        ObservacionTrabajo = findViewById(R.id.TxtObservacionesTexto);
        FechaHoraInicioActividad = findViewById(R.id.TxtHoraInicioActividad);
        FechaHoraTerminoActividad = findViewById(R.id.TxtHoraTerminoActividad);
        TiempoTotalActividad = findViewById(R.id.TxtTotalActividad);

        // Tipo Botones
        GuardarRecorrido = findViewById(R.id.BtnGuardarRecorrido);

        //Tipo Numero
        Eventos = findViewById(R.id.TxtNumeroEvento);


        //Tipo Radio Button
        TipoClienteTroncal = findViewById(R.id.RdTroncal);
        TipoClienteUltimaMilla = findViewById(R.id.RdUltimaMilla);
        CerrarModificacionSi = findViewById(R.id.RdCerrarModificacionSi);
        CerrarModificacionNo = findViewById(R.id.RdCerrarModificacionNo);

        //Tipo Radio Group

        GrupoTipoCliente = findViewById(R.id.RgTipoCliente);
        GrupoCerrarModificacion = findViewById(R.id.RgCerrarModificacion);


        //Tipo Spinner
        SupervisorTurno = findViewById(R.id.SpSupervisor);
        TecnicoReparacion = findViewById(R.id.SpTecnicoReparacion);
        TecnicoReparacion2 = findViewById(R.id.SpTecnicoReparacionOpcional);
        ZonaMantenimiento = findViewById(R.id.SpZonaMantenimiento);
        ClienteAfectado = findViewById(R.id.SpClienteAfectado);


        //Llamar Metodos
        ListadoSupervisorTurno();
        ListadoTecnicoReparacion();
        ListadoTecnicoReparacionOpcional();
        ListadoZonaMantenimiento();
        ListadoNombre();


        //Metodo para obtener Fecha de creacion de formulario
        FechaRecorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar ObtencionFecha = Calendar.getInstance();
                Years = ObtencionFecha.get(Calendar.YEAR);
                Mes = ObtencionFecha.get(Calendar.MONTH);
                Dia = ObtencionFecha.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog RecibirFecha = new DatePickerDialog(FormularioRellenar.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAYS) {
                        Years = YEAR;
                        Mes = MONTH + 1;
                        Dia = DAYS;
                        FechaRecorrido.setText(Dia + "/" + Mes + "/" + Years);
                    }
                }, Years, Mes, Dia);
                RecibirFecha.show();
            }
        });

        //Metodo para obtener el inicio de la actividad del recorrido
        FechaHoraInicioActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar CalendarioInicioActividad = Calendar.getInstance();
                Hora = CalendarioInicioActividad.get(Calendar.HOUR_OF_DAY);
                Minutos = CalendarioInicioActividad.get(Calendar.MINUTE);
                YearsI = CalendarioInicioActividad.get(Calendar.YEAR);
                MesI = CalendarioInicioActividad.get(Calendar.MONTH);
                DiaI = CalendarioInicioActividad.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog ObtenerFechaInicio = new DatePickerDialog(FormularioRellenar.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int Years1, int Month1, int Dyas1) {
                        YearsI = Years1;
                        MesI = Month1 + 1;
                        DiaI = Dyas1;
                        FechaHoraInicioActividad.setText(DiaI + "/" + MesI + "/" + YearsI + "\n" + Hora + ":" + Minutos);
                    }
                }, YearsI, MesI, DiaI);
                TimePickerDialog ObtenerHoraInicio = new TimePickerDialog(FormularioRellenar.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int HoraTomar, int MinutoTomar) {
                        Hora = HoraTomar;
                        Minutos = MinutoTomar;
                    }
                }, Hora, Minutos, false);
                ObtenerFechaInicio.show();
                ObtenerHoraInicio.show();
            }
        });

        //Metodo para obtener la finalizacion de la actividad del recorrido
        FechaHoraTerminoActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar CalendarioTerminoActividad = Calendar.getInstance();
                HoraT = CalendarioTerminoActividad.get(Calendar.HOUR_OF_DAY);
                MinutosT = CalendarioTerminoActividad.get(Calendar.MINUTE);
                YearsT = CalendarioTerminoActividad.get(Calendar.YEAR);
                MesT = CalendarioTerminoActividad.get(Calendar.MONTH);
                DiaT = CalendarioTerminoActividad.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog ObtenerFechaTermino = new DatePickerDialog(FormularioRellenar.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int AnoTermino, int MesTermino, int DiaTermino) {
                        YearsT = AnoTermino;
                        MesT = MesTermino + 1;
                        DiaT = DiaTermino;
                        FechaHoraTerminoActividad.setText(DiaT + "/" + MesT + "/" + YearsT + "\n" + HoraT + ":" + MinutosT);
                    }
                }, YearsT, MesT, DiaT);
                TimePickerDialog ObtenerHoraTermino = new TimePickerDialog(FormularioRellenar.this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int HoraTerminar, int MinutoTerminar) {
                        HoraT = HoraTerminar;
                        MinutosT = MinutoTerminar;
                    }
                }, Hora, Minutos, false);
                ObtenerFechaTermino.show();
                ObtenerHoraTermino.show();
            }
        });


        //Mensajes Para Radio Button



        GrupoTipoCliente.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ChecarCliente) {
                if (ChecarCliente == R.id.RdTroncal) {
                    Toast.makeText(FormularioRellenar.this, "Tipo De Cliente = Troncal", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ChecarCliente == R.id.RdUltimaMilla) {
                    Toast.makeText(FormularioRellenar.this, "Tipo De Cliente = Ultima Milla", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        GrupoCerrarModificacion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ChecarModificacion) {
                if (ChecarModificacion == R.id.RdCerrarModificacionSi) {
                    Toast.makeText(FormularioRellenar.this, "Cerrar Modificacion = Si", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ChecarModificacion == R.id.RdCerrarModificacionNo) {
                    Toast.makeText(FormularioRellenar.this, "Cerrar Modificacion = No", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        //Validacion Para Boton Guardar Recorrido
        GuardarRecorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent IntentoGuardarRecorrido = new Intent(FormularioRellenar.this, MainActivity.class);

                if (TituloRecorrido.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Titulo Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (FechaRecorrido.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Fecha Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Eventos.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo N° Evento Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rutSup.equals("")) {
                    Toast.makeText(FormularioRellenar.this, "Campo Supervisor Vacio", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (SupervisorTurno.getSelectedItemPosition() == 0) {
                    Toast.makeText(FormularioRellenar.this, "Campo Supervisor 1 Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TecnicoReparacion.getSelectedItemPosition() == 0) {
                    Toast.makeText(FormularioRellenar.this, "Campo Tecnico Reparacion Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ZonaMantenimiento.getSelectedItemPosition() == 0) {
                    Toast.makeText(FormularioRellenar.this, "Campo Zona Mantenimiento Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rutTecRep1.equals("")) {
                    Toast.makeText(FormularioRellenar.this, "Campo Tecnico Reparación Vacio", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Zonaman.equals("")) {
                    Toast.makeText(FormularioRellenar.this, "Campo Zona Mantenimiento Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (FechaHoraInicioActividad.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Fecha Hora Inicio Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (FechaHoraTerminoActividad.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Fecha Hora Termino Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TiempoTotalActividad.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Tiempo Total Actividad Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ClienteAfectado.getSelectedItemPosition() == 0) {
                    Toast.makeText(FormularioRellenar.this, "Campo Cliente Afectado Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GrupoTipoCliente.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(FormularioRellenar.this, "Campo Tipo Cliente Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (LocalizacionFalla.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Localización Falla Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GrupoCerrarModificacion.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(FormularioRellenar.this, "Campo Cerrar Modificación Vacio", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DescribirMateriales.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Descripción Material Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (DescribirTrabajo.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Descripción Trabajo Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ResolucionTrabajo.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Resolución Trabajo Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ObservacionTrabajo.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Observación Vació", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //String Radio Group
                    int SelectCliente = GrupoTipoCliente.getCheckedRadioButtonId();
                    RadioButton RbCliente = findViewById(SelectCliente);
                    String EleccionCliente = RbCliente.getText().toString();

                    int SelectCerrMod = GrupoCerrarModificacion.getCheckedRadioButtonId();
                    RadioButton RbCerrarMod = findViewById(SelectCerrMod);
                    String EleccionCerrModif = RbCerrarMod.getText().toString();

                    //FormularioDB dbFormulario = new FormularioDB(FormularioRellenar.this);
                    try {
                        Formulario form = new Formulario();
                        form.setTitulo_Form(TituloRecorrido.getText().toString());
                        form.setFecha_Form(FechaRecorrido.getText().toString());
                        form.setNumero_Evento(Eventos.getText().toString());
                        form.setSupervisor_Turno(rutSup);
                        form.setTecnico_Reparacion(rutTecRep1);
                        form.setTecnico_Reparacion(rutTecRep2);
                        form.setZona_Mantenimiento(Zonaman);
                        form.setFecha_Inicio(FechaHoraInicioActividad.getText().toString());
                        form.setFecha_Termino(FechaHoraTerminoActividad.getText().toString());
                        form.setTiempo_Total_Actividad(TiempoTotalActividad.getText().toString());
                        form.setCliente_Afectado(ClienteAfectado.getSelectedItem().toString());
                        form.setTipo_Cliente(EleccionCliente);
                        form.setLocalizacion_Falla(LocalizacionFalla.getText().toString());
                        form.setCerrar_Modificacion(EleccionCerrModif);
                        form.setDescripcion_Materiales(DescribirMateriales.getText().toString());
                        form.setDescripcion_Trabajo(DescribirTrabajo.getText().toString());
                        form.setResolucion_Tabajo(ResolucionTrabajo.getText().toString());
                        form.setObservaciones(ObservacionTrabajo.getText().toString());

                        /*
                                ELeccionFotoAntes,
                                EleccionFotoFinal,
                                */
                        dbform.InsercionFormulario(form);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Se genera mensaje de posteo de formulario y este es almacenado en la base local del telefono
                    //PublicarPost();
                    startActivity(IntentoGuardarRecorrido);
                    Toast.makeText(FormularioRellenar.this, "Registro De Recorrido Guardado", Toast.LENGTH_SHORT).show();
                    LimpiarCuadro();
                    return;
                }

            }
        });


        //Obtener datos de operarios y bodegas mediante metodo volley
        OperariosObtenerVolley();


    }


    //Crear Metodos

    public void ListadoSupervisorTurno() {
        ArrayList<String> SupervisoresT = new ArrayList<>();
        SupervisoresT.add("Juvenal Eduardo Rivas Gallardo");
        SupervisoresT.add("Ismael Ernesto Castillo Estrada");
        SupervisoresT.add("Luis Rodriguez");
        SupervisoresT.add("Luis Ermi Aguilar Alfaro");
        SupervisoresT.add("Boris Antonio Campos Rivera");
        SupervisoresT.add("Den Ivar Sanhueza Iturra");
        SupervisoresT.add("Esteban Avila");
        SupervisoresT.add("Gonzalo Mauricio Zapata Gavilan");
        SupervisoresT.add("Diego Macaya Alea");
        SupervisoresT.add("Matias Henriquez Clavet");
        SupervisoresT.add("Eduardo Raul Mendez");
        SupervisoresT.add("Nestor Alejandro Rapetti");
        SupervisoresT.add("Alex Navarrete Inst. Tel y Com E.I.R.L");
        SupervisoresT.add("Erick Catalan Perez TeleCom. E.I.R.L");
        SupervisoresT.add("Retro Ingenieria Spa");
        SupervisoresT.add("Jose Calfunao Cyenet TeleCom. Cabello Urbina Spa");
        SupervisoresT.add("Angelo-Af TeleCom.");
        SupervisoresT.add("Pablo Rodriguez Hr TeleCom. Ser. Fibra Optica Integral");
        SupervisoresT.add("Juan Bravo Alvial Claudio Bravo Alvial Ing. Com. y Tel.");
        SupervisoresT.add("Feretel Spa Reveco");
        SupervisoresT.add("Pablo Cid Montero Propext Soc. Ing. y Constr. Telefonica");
        SupervisoresT.add("Marco Antonio Hernandez Navarrete Inst. y Tendido Fibra Opt.");
        SupervisoresT.add("Edgardo Vasquez Mitchell Selevec Spa");
        ArrayAdapter<String> ListadoS = new ArrayAdapter<>(FormularioRellenar.this, android.R.layout.simple_list_item_1, SupervisoresT);
        SupervisorTurno.setAdapter(ListadoS);
    }

    public void ListadoTecnicoReparacion() {
        ArrayList<String> TecnicoRepar = new ArrayList<>();
        TecnicoRepar.add("Juvenal Eduardo Rivas Gallardo");
        TecnicoRepar.add("Ismael Ernesto Castillo Estrada");
        TecnicoRepar.add("Luis Rodriguez");
        TecnicoRepar.add("Luis Ermi Aguilar Alfaro");
        TecnicoRepar.add("Boris Antonio Campos Rivera");
        TecnicoRepar.add("Den Ivar Sanhueza Iturra");
        TecnicoRepar.add("Esteban Avila");
        TecnicoRepar.add("Gonzalo Mauricio Zapata Gavilan");
        TecnicoRepar.add("Diego Macaya Alea");
        TecnicoRepar.add("Matias Henriquez Clavet");
        TecnicoRepar.add("Eduardo Raul Mendez");
        TecnicoRepar.add("Nestor Alejandro Rapetti");
        TecnicoRepar.add("Alex Navarrete Inst. Tel y Com E.I.R.L");
        TecnicoRepar.add("Erick Catalan Perez TeleCom. E.I.R.L");
        TecnicoRepar.add("Retro Ingenieria Spa");
        TecnicoRepar.add("Jose Calfunao Cyenet TeleCom. Cabello Urbina Spa");
        TecnicoRepar.add("Angelo-Af TeleCom.");
        TecnicoRepar.add("Pablo Rodriguez Hr TeleCom. Ser. Fibra Optica Integral");
        TecnicoRepar.add("Juan Bravo Alvial Claudio Bravo Alvial Ing. Com. y Tel.");
        TecnicoRepar.add("Feretel Spa Reveco");
        TecnicoRepar.add("Pablo Cid Montero Propext Soc. Ing. y Constr. Telefonica");
        TecnicoRepar.add("Marco Antonio Hernandez Navarrete Inst. y Tendido Fibra Opt.");
        TecnicoRepar.add("Edgardo Vasquez Mitchell Selevec Spa");
        ArrayAdapter<String> ListadoR = new ArrayAdapter<>(FormularioRellenar.this, android.R.layout.simple_list_item_1, TecnicoRepar);
        TecnicoReparacion.setAdapter(ListadoR);
    }

    public void ListadoTecnicoReparacionOpcional() {
        ArrayList<String> TecnicoReparOpc = new ArrayList<>();
        TecnicoReparOpc.add("No Requiere");
        TecnicoReparOpc.add("Juvenal Eduardo Rivas Gallardo");
        TecnicoReparOpc.add("Ismael Ernesto Castillo Estrada");
        TecnicoReparOpc.add("Luis Rodriguez");
        TecnicoReparOpc.add("Luis Ermi Aguilar Alfaro");
        TecnicoReparOpc.add("Boris Antonio Campos Rivera");
        TecnicoReparOpc.add("Den Ivar Sanhueza Iturra");
        TecnicoReparOpc.add("Esteban Avila");
        TecnicoReparOpc.add("Gonzalo Mauricio Zapata Gavilan");
        TecnicoReparOpc.add("Diego Macaya Alea");
        TecnicoReparOpc.add("Matias Henriquez Clavet");
        TecnicoReparOpc.add("Eduardo Raul Mendez");
        TecnicoReparOpc.add("Nestor Alejandro Rapetti");
        TecnicoReparOpc.add("Alex Navarrete Inst. Tel y Com E.I.R.L");
        TecnicoReparOpc.add("Erick Catalan Perez TeleCom. E.I.R.L");
        TecnicoReparOpc.add("Retro Ingenieria Spa");
        TecnicoReparOpc.add("Jose Calfunao Cyenet TeleCom. Cabello Urbina Spa");
        TecnicoReparOpc.add("Angelo-Af TeleCom.");
        TecnicoReparOpc.add("Pablo Rodriguez Hr TeleCom. Ser. Fibra Optica Integral");
        TecnicoReparOpc.add("Juan Bravo Alvial Claudio Bravo Alvial Ing. Com. y Tel.");
        TecnicoReparOpc.add("Feretel Spa Reveco");
        TecnicoReparOpc.add("Pablo Cid Montero Propext Soc. Ing. y Constr. Telefonica");
        TecnicoReparOpc.add("Marco Antonio Hernandez Navarrete Inst. y Tendido Fibra Opt.");
        TecnicoReparOpc.add("Edgardo Vasquez Mitchell Selevec Spa");
        ArrayAdapter<String> ListadoRop = new ArrayAdapter<>(FormularioRellenar.this, android.R.layout.simple_list_item_1, TecnicoReparOpc);
        TecnicoReparacion2.setAdapter(ListadoRop);
    }

    public void ListadoZonaMantenimiento() {
        ArrayList<String> ZonaMante = new ArrayList<>();
        ZonaMante.add("Concepción");
        ZonaMante.add("Talca");
        ZonaMante.add("Rancagua");
        ZonaMante.add("Osorno-Pto Montt");
        ZonaMante.add("Temuco");
        ZonaMante.add("Santiago");
        ZonaMante.add("Bodega Central");
        ArrayAdapter<String> ListadoZona = new ArrayAdapter<>(FormularioRellenar.this, android.R.layout.simple_list_item_1, ZonaMante);
        ZonaMantenimiento.setAdapter(ListadoZona);
    }

    public void ListadoNombre() {
        ArrayList<String> ListarClientes = new ArrayList<>();
        ListarClientes.add("Ac Comunicaciones Chillán");
        ListarClientes.add("AnacondaWeb");
        ListarClientes.add("Anillo Silica");
        ListarClientes.add("Silica (Nodo) Cabrero");
        ListarClientes.add("Century Link");
        ListarClientes.add("Colun -Dos Sur");
        ListarClientes.add("Colun - Ohiggin´s");
        ListarClientes.add("Colun - KM 1");
        ListarClientes.add("Colun");
        ListarClientes.add("Colun - Ruta 5");
        ListarClientes.add("Colun CLN001");
        ListarClientes.add("Colun CLN022");
        ListarClientes.add("Colun CLN023");
        ListarClientes.add("CTR Puerto Montt");
        ListarClientes.add("Anmax");
        ListarClientes.add("ElectroRed");
        ListarClientes.add("Entel Puerto Montt");
        ListarClientes.add("Frigosorno Acceso a Internet y MPLS Ruta 2");
        ListarClientes.add("Frigosorno Acceso a Internet y MPLS Ruta 1");
        ListarClientes.add("Frigosorno Sala De Ventas Puerto Montt");
        ListarClientes.add("Full Connection");
        ListarClientes.add("Geonet Talca 1");
        ListarClientes.add("Geonet Talca 2");
        ListarClientes.add("Geonet - Temuco");
        ListarClientes.add("Ingreso Entel Osorno (Soterrado)");
        ListarClientes.add("Ingreso Nodo Movistar");
        ListarClientes.add("Cable Lautaro");
        ListarClientes.add("MultiExport SLM004");
        ListarClientes.add("Mufa Cliente Entel");
        ListarClientes.add("Ranco");
        ListarClientes.add("RedCom RDC002");
        ListarClientes.add("U. Del Bio Bio");
        ListarClientes.add("Ruta IntraCom Osorno A Mufa Final Troncal");
        ListarClientes.add("Ruta Rio Bueno-Los Tambores(IntraCom)");
        ListarClientes.add("Reuna");
        ListarClientes.add("Sala De Venta Frigosorno");
        ListarClientes.add("Cliente San Vicente");
        ListarClientes.add("Cliente Peumo 1");
        ListarClientes.add("Cliente Peumo 2");
        ListarClientes.add("U. Talca");
        ListarClientes.add("Escuela Mollulco");
        ListarClientes.add("Escuela Rucapangui");
        ListarClientes.add("Jardin Los Pumitas");
        ListarClientes.add("Troncal a Colum");
        ListarClientes.add("Troncal Apertura Puerto Montt");
        ListarClientes.add("Troncal Apertura Rio Bueno");
        ListarClientes.add("Tv Cable Nacimiento");
        ListarClientes.add("U. De Concepción");
        ListarClientes.add("U. De Rancagua UOH");
        ListarClientes.add("Ingesur");
        ListarClientes.add("Luz Linares");
        ListarClientes.add("U.De Talca Ruta Primaria");
        ListarClientes.add("Geonet Talca Ruta Secundaria U. De Talca");
        ListarClientes.add("Sika Concepción");
        ListarClientes.add("U. De La Frontera");
        ListarClientes.add("Ultima Milla AcuaChile");
        ListarClientes.add("Ultima Milla Netxion");
        ListarClientes.add("Ruta Troncal Collipulli-Temuco");
        ListarClientes.add("Ruta Troncal Cabrero-Los Angeles");
        ListarClientes.add("Ruta Troncal Los Angeles-Collipulli");
        ListarClientes.add("Enlace Rancagua-Graneros");
        ListarClientes.add("Instituto Profesional Los Lagos");
        ListarClientes.add("Troncal Nodo Puerto Montt-Alerce");
        ListarClientes.add("Troncal Osorno-P.Montt (Silica-Silica");
        ListarClientes.add("Ruta Osorno IntraCom-Rio Bueno");
        ListarClientes.add("Temuco-Cholchol");
        ListarClientes.add("CFT Los Lagos-Osorno Casa Central");
        ListarClientes.add("Frigosorno Temuco");
        ListarClientes.add("Lumen Santiago");
        ListarClientes.add("CFT Los Lagos-Temuco");
        ListarClientes.add("Net Del Sur-Cliente Las Cabras");
        ListarClientes.add("Pop Osorno-Los Tambores Nuevo Kable ");
        ListarClientes.add("Troncal Pop Osorno-Entel(Soterrado)");
        ListarClientes.add("Troncal Pop Osorno-Regimiento");
        ListarClientes.add("Ruta Troncal Temuco-Freire");
        ListarClientes.add("Century Tilcoco");
        ListarClientes.add("Coltauro Colatauco-Doñihue");
        ListarClientes.add("Troncal Doñihue-Rancagua");
        ListarClientes.add("Troncal Cardonal-DataCenter GTD");
        ListarClientes.add("Troncal Puerto Montt-Movistar y Entel");
        ListarClientes.add("Troncal Los Tambores-La Unión");
        ListarClientes.add("Cliente Fuera De Mantenimiento");
        ListarClientes.add("Silica Networks S.A");
        ListarClientes.add("EDGEConnex");
        ListarClientes.add("CFT Los Lagos-Talca");
        ListarClientes.add("CFT Los Lagos-Los Angeles");
        ListarClientes.add("CFT Los Lagos-Rancagua");
        ListarClientes.add("CFT Los Lagos-San Fernando");
        ListarClientes.add("AU. Central-Pop Santiago(Compañia)");
        ListarClientes.add("UM. Frigosorno-Talca");
        ListarClientes.add("UM. Frigosorno-Rancagua");
        ListarClientes.add("Sonda");
        ListarClientes.add("UM Ufro Temuco");
        ListarClientes.add("Enlace Wom Victoria");
        ListarClientes.add("Enlace Temuco-Pucon");
        ListarClientes.add("Telbros");
        ListarClientes.add("Ascenty");
        ArrayAdapter<String> ListarClientesRs = new ArrayAdapter<>(FormularioRellenar.this, android.R.layout.simple_list_item_1, ListarClientes);
        ClienteAfectado.setAdapter(ListarClientesRs);
    }

    public void LimpiarCuadro() {
        TituloRecorrido.setText("");
        FechaRecorrido.setText("");
        //GrupoEventos.setSelected(false);
        Eventos.setText("");
        //GrupoPerdidaServicio.clearCheck();
        SupervisorTurno.setSelection(0);
        TecnicoReparacion.setSelection(0);
        TecnicoReparacion2.setSelection(0);
        ZonaMantenimiento.setSelection(0);
        FechaHoraInicioActividad.setText("");
        FechaHoraTerminoActividad.setText("");
        ClienteAfectado.setSelection(0);
        GrupoTipoCliente.clearCheck();
        LocalizacionFalla.setText("");
        //DistanciaOptica.setText("");
        //Latitud.setText("");
        //Longitud.setText("");
        //GrupoRegistroFotoAntes.clearCheck();
        //GrupoRegistroFotoFinal.clearCheck();
        //GrupoRegistroMedicionAntes.clearCheck();
        //GrupoRegistroMedicionFinal.clearCheck();
        GrupoCerrarModificacion.clearCheck();
        DescribirMateriales.setText("");
        DescribirTrabajo.setText("");
        ResolucionTrabajo.setText("");
        ObservacionTrabajo.setText("");


    }

    public void ObtenerTiempo(String Hora1, String Hora2) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            Date startDate = simpleDateFormat.parse(Hora1);
            Date endDate = simpleDateFormat.parse(Hora2);
            long difference = endDate.getTime() - startDate.getTime();
            if (difference < 0) {
                Date dateMax = simpleDateFormat.parse(Hora1);
                Date dateMin = simpleDateFormat.parse(Hora2);
                difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
            }
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            Log.i("log_tag", "Hours: " + hours + ", Mins: " + min);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    //Creacion de obtencion de datos Almacenados en Base de datos de operarios y bodega
    //Carga listado de operarios y bodega
    private Datosform datosform;

    private List<Operarios> listop = new ArrayList<>();
    private List<Bodegas> listBod = new ArrayList<>();

    public void OperariosObtenerVolley() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLWEB + "api/Formulario/datosform", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //datosform.setOperarios( Gson.fromJson(response.getJSONArray("Operarios"), Datosform.Operarios.class));
                    JSONArray j = response.getJSONArray("Operarios");
                    ArrayList<String> TecnicoRepar = new ArrayList<>();
                    ArrayList<String> SupervisoresT = new ArrayList<>();
                    ArrayList<String> TecnicoReparOpc = new ArrayList<>();
                    for (int i = 0; i < j.length(); i++) {
                        try {
                            Operarios op = new Operarios();
                            JSONObject obj = j.getJSONObject(i);
                            op.setRut(obj.getInt("Rut"));
                            listop.add(op);
                            SupervisoresT.add(obj.getString("Nombre"));
                            TecnicoRepar.add(obj.getString("Nombre"));
                            TecnicoReparOpc.add(obj.getString("Nombre"));
                            //op.setNombre();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    /***/
                    ArrayAdapter<String> ListadoR = new ArrayAdapter<>(FormularioRellenar.this, android.R.layout.simple_list_item_1, TecnicoRepar);
                    TecnicoReparacion.setAdapter(ListadoR);

                    ArrayAdapter<String> ListadoS = new ArrayAdapter<>(FormularioRellenar.this, android.R.layout.simple_list_item_1, SupervisoresT);
                    SupervisorTurno.setAdapter(ListadoS);

                    ArrayAdapter<String> ListadoRop = new ArrayAdapter<>(FormularioRellenar.this, android.R.layout.simple_list_item_1, TecnicoReparOpc);
                    TecnicoReparacion2.setAdapter(ListadoRop);

                    SupervisorTurno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Operarios op = listop.get(position);
                            rutSup = String.valueOf(op.getRut());

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    TecnicoReparacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Operarios op = listop.get(position);

                            rutTecRep1 = String.valueOf(op.getRut());
                            //Log.e("OP-->", ""+op.getRut());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    TecnicoReparacion2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Operarios op = listop.get(position);

                            rutTecRep2 = String.valueOf(op.getRut());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /**/
                //JSONArray j = null;
                ArrayList<String> ZonaMante = new ArrayList<>();
                try {
                    JSONArray j = response.getJSONArray("Bodegas");
                    for (int i = 0; i < j.length(); i++) {
                        Bodegas bg = new Bodegas();
                        JSONObject obj = j.getJSONObject(i);
                        bg.setCodigo((byte) obj.getInt("Codigo"));
                        listBod.add(bg);
                        ZonaMante.add(obj.getString("Nombre"));
                    }

                    ArrayAdapter<String> ListadoZona = new ArrayAdapter<String>(FormularioRellenar.this, android.R.layout.simple_list_item_1, ZonaMante);
                    ZonaMantenimiento.setAdapter(ListadoZona);

                    ZonaMantenimiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Bodegas bdo = listBod.get(position);
                            Zonaman = String.valueOf(bdo.getCodigo());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
    }

    private View.OnClickListener TotalActividad = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            Integer month=c.get(Calendar.MONTH);
            Integer day=c.get(Calendar.DAY_OF_MONTH);
            Integer year=c.get(Calendar.YEAR);


            if(FechaHoraInicioActividad.getText().length() >0){
                DatePickerDialog datePickerDialog = new DatePickerDialog(FormularioRellenar.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(FormularioRellenar.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if((month+1) > 9){
                                    if(minute > 9){
                                        FechaHoraTerminoActividad.setText(dayOfMonth + "/" + (month + 1) + "/" + year + " " + hourOfDay + ":" + minute);
                                            }else{
                                        FechaHoraTerminoActividad.setText(dayOfMonth + "/" + (month + 1) + "/" + year + " " + hourOfDay+":0"+minute);
                                            }
                                        }else{
                                    if(minute > 9){
                                        FechaHoraTerminoActividad.setText(dayOfMonth+"/0"+(month+1)+"/"+year+" "+ hourOfDay + ":" + minute);
                                    }else{
                                        FechaHoraTerminoActividad.setText(dayOfMonth+"/0"+(month+1)+"/"+year+" "+ hourOfDay+":0"+minute);
                                            }
                                    try {
                                        String time1 = MesI+"-"+DiaI+"-"+YearsI+" "+Hora+":"+Minutos;
                                        String time2 = month+"-"+dayOfMonth+"-"+year+" "+hourOfDay+":"+minute;
                                        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                                        Date date1 = format.parse(time1);
                                        Date date2 = format.parse(time2);
                                        long difference = date2.getTime() - date1.getTime();
                                        long diffDays = (difference / (1000 * 60 * 60 * 24)) % 365;
                                        long diffMinutes = difference / (60 * 1000) % 60;
                                        long diffHours = difference / (60 * 60 * 1000) % 24;
                                        if(diffHours < 0 || diffDays < 0){
                                            alertDialog.setTitle("Alerta");
                                            alertDialog.setMessage("La hora o fecha no puede ser menor a la de inicio");
                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                            alertDialog.dismiss();
                                                        }
                                                    });
                                                    alertDialog.show();
                                                    TiempoTotalActividad.setText("");
                                                    return;
                                                }
                                                TiempoTotalActividad.setText(diffHours+" Horas"+" "+diffMinutes+" Minutos "+diffDays+" Dias" );
                                            }catch (ParseException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }, HoraCompleto, MinutosCompleto, false);
                                timePickerDialog.show();
                            }
                        }, YearsI, MesI, DiaI);
                        datePickerDialog.show();
                    }else{
                        alertDialog.setTitle("Alerta");
                        alertDialog.setMessage("Falta fecha hora de inicio actividad");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                }
        };
}







