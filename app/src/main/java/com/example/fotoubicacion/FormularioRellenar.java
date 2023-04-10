package com.example.fotoubicacion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.fotoubicacion.DB.DBHelper;
import com.example.fotoubicacion.DB.FormularioDB;
import com.example.fotoubicacion.Models.Bodegas;
import com.example.fotoubicacion.Models.Clientes;
import com.example.fotoubicacion.Models.Datosform;
import com.example.fotoubicacion.Models.Formulario;
import com.example.fotoubicacion.Models.Operarios;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

public class FormularioRellenar extends AppCompatActivity {


    //Url Conectado para envio de datos a servidor
   // private static final String URL2 = "http://192.168.0.167:50326/";
    //private static final String URLWEB = "http://captel_api.openpanel.cl/swagger/ui/index";

    private RequestQueue queue;

    //Establecemos Comunicacion Con Sqlite
    SQLiteDatabase sqLiteDb;
    //
    FormularioDB dbform;
    DBHelper DB;

    Calendar fechai, Fechat;

    //Definicion de tipos datos entrantes
    EditText TituloRecorrido, FechaRecorrido, LocalizacionFalla, DescribirTrabajo, ResolucionTrabajo,
            ObservacionTrabajo, DescribirMateriales, FechaHoraInicioActividad,
            FechaHoraTerminoActividad, TiempoTotalActividad, Txtbuscar;

    ListView Listbuscar;

    RadioButton TipoClienteTroncal, TipoClienteUltimaMilla;

    RadioGroup GrupoTipoCliente;

    Spinner SupervisorTurno, TecnicoReparacion, TecnicoReparacionOP, ZonaMantenimiento, ClienteAfectado;

    Button GuardarRecorrido,Btnfiltrar;

    AlertDialog alertDialog;

    private Datosform datosform;

    int registro_formulario=0;

    ArrayList<String> SupervisoresT = new ArrayList<>();
    ArrayList<Integer> rutSupervisoresT = new ArrayList<>();
    ArrayList<String> TecnicoRepar = new ArrayList<>();
    ArrayList<Integer> rutTecnicoRepar = new ArrayList<>();
    ArrayList<String> TecnicoRepar2 = new ArrayList<>();
    ArrayList<Integer> rutTecnicoRepar2 = new ArrayList<>();
    ArrayList<String> ZonaMante = new ArrayList<>();
    ArrayList<String> Clientes = new ArrayList<>();

    ArrayAdapter<String> ListadoClientes;

    private List<Integer> listop = new ArrayList<>();
    private List<Integer> listzo = new ArrayList<>();
    private List<Integer> listcl = new ArrayList<>();
    private List<Integer> listclcopia = new ArrayList<>();

    //-Definir tipo de datos para adaptar fecha y hora
    int Hora, Minutos, HoraT, MinutosT, HoraCompleto, MinutosCompleto;
    int Dia, Mes, Years, DiaT, MesT, YearsT, DiaI, MesI, YearsI;
    int mHour, mMinute, year, month, day;

    private String rutSup = "", rutTecRep1 = "", rutTecRep2 = "";
    private String Zonaman = "", Referencia_Cliente="";

    private ArrayAdapter<String> adapter;

    private String products[] = {"Apple", "Banana","Pinapple", "Orange", "Papaya", "Melon",
            "Grapes", "Water Melon","Lychee", "Guava", "Mango", "Kivi"};

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_rellenar);
        getSupportActionBar().hide();

        dbform = new FormularioDB(FormularioRellenar.this);
        queue = Volley.newRequestQueue(FormularioRellenar.this);

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
        GuardarRecorrido = findViewById(R.id.BtnGuardarRecorrido);

        Txtbuscar = findViewById(R.id.Txtbuscar);

        Btnfiltrar = findViewById(R.id.Btnfiltrar);
        Btnfiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = String.valueOf(Txtbuscar.getText());
                if (!ListadoClientes.equals(null)) {
                    ListadoClientes.getFilter().filter(texto);
                }
                listcl=new ArrayList<>();
                int l = ListadoClientes.getCount();
                if (l>0)
                {
                    int k=0;
                    for (int i = 0; i < l; i++) {
                        String nombre=(String)ListadoClientes.getItem(i);
                        int p = Clientes.indexOf(nombre);
                        listcl.add(i,listclcopia.get(p));
                    }
                }

            }
        });
        Btnfiltrar.setBackgroundColor(Color.WHITE);
        Btnfiltrar.setTextColor(Color.BLACK);

        ClienteAfectado = findViewById(R.id.SpClienteAfectado);

        TipoClienteTroncal = findViewById(R.id.RdTroncal);
        TipoClienteUltimaMilla = findViewById(R.id.RdUltimaMilla);
        GrupoTipoCliente = findViewById(R.id.RgTipoCliente);
        SupervisorTurno = findViewById(R.id.SpSupervisor);
        TecnicoReparacion = findViewById(R.id.SpTecnicoReparacion);
        TecnicoReparacionOP = findViewById(R.id.SpTecnicoReparacionOpcional);
        ZonaMantenimiento = findViewById(R.id.SpZonaMantenimiento);

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
        TiempoTotalActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String FechaInicio = FechaHoraInicioActividad.getText().toString();
                String FechaTermino = FechaHoraTerminoActividad.getText().toString();
                SimpleDateFormat Simplefecha = new SimpleDateFormat("yyyy/MM/dd");


                try{
                    Date fecha1 = Simplefecha.parse(FechaInicio);
                    Date fecha2 = Simplefecha.parse(FechaTermino);
                    Date comienzo = Simplefecha.parse(FechaInicio);
                    Date termino = Simplefecha.parse(FechaTermino);
                    long startdate = fecha1.getTime();
                    long finishdate = fecha2.getTime();
                    long Horacom = comienzo.getTime();
                    long Horater = termino.getTime();



                    if(startdate <= finishdate ){
                        Period periodo = new Period(startdate,finishdate,PeriodType.yearMonthDay());
                        Period periodohora = new Period(Horacom, Horater, PeriodType.hours());
                        Period periodomin = new Period(Horacom, Horater, PeriodType.minutes());

                        int ano1 = periodo.getYears();
                        int Mes1 = periodo.getMonths();
                        int Minutostotal = periodomin.getMinutes()/60/1024;
                        int Horatotal = periodohora.getHours()/60/10;


                        TiempoTotalActividad.setText("Dias : "+ ano1 + " / " + "Mes :" +  Mes1 + " / "+ "Tiempo = "+ Horatotal + ":" + Minutostotal);


                    }
                }catch(ParseException e){

                }
            }
        });
        GrupoTipoCliente.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ChecarCliente) {
                if (ChecarCliente == R.id.RdTroncal) {
                    return;
                } else if (ChecarCliente == R.id.RdUltimaMilla) {
                    return;
                }
            }
        });
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
                if (rutSup.equals("")) {
                    Toast.makeText(FormularioRellenar.this, "Campo Supervisor Vacio", Toast.LENGTH_SHORT).show();
                    return;
                }
                String supervisorturno=SupervisorTurno.getSelectedItem().toString();
                if (supervisorturno.isEmpty()) {
                    String eleccion = SupervisorTurno.getSelectedItem().toString();
                    Toast.makeText(FormularioRellenar.this, "Campo Supervisor 1 Vació"+eleccion, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rutTecRep1.equals("")) {
                    Toast.makeText(FormularioRellenar.this, "Campo Tecnico Reparación Vacio", Toast.LENGTH_SHORT).show();
                    return;
                }
                String tecnicoreparacion=TecnicoReparacion.getSelectedItem().toString();
                if (tecnicoreparacion.isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Tecnico Reparacion Vació", Toast.LENGTH_SHORT).show();
                    return;
                }

                String zonamantenimiento=ZonaMantenimiento.getSelectedItem().toString();
                if (zonamantenimiento.isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Zona Mantenimiento Vació", Toast.LENGTH_SHORT).show();
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
                if (ClienteAfectado.getCount()==0){
                    Toast.makeText(FormularioRellenar.this, "Campo Cliente Afectado Vació", Toast.LENGTH_SHORT).show();
                return;
                }
                String clienteafectado=ClienteAfectado.getSelectedItem().toString();
                //if (ClienteAfectado.getSelectedItemPosition() == 0) {
                if (clienteafectado.isEmpty()) {
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
                    Log.i("Tipo Cliente guardar :",String.valueOf(SelectCliente));
                    RadioButton RbCliente = findViewById(SelectCliente);
                    String EleccionCliente = RbCliente.getText().toString();
                    try {
                        Formulario form = new Formulario();
                        form.setRegistro(registro_formulario);
                        form.setTitulo_Form(TituloRecorrido.getText().toString());
                        form.setFecha_Form(FechaRecorrido.getText().toString());
                        form.setSupervisor_Turno(rutSup);
                        form.setTecnico_Reparacion(rutTecRep1);
                        form.setTecnico_ReparacionOP(rutTecRep2);
                        form.setZona_Mantenimiento(Zonaman);
                        form.setFecha_Inicio(FechaHoraInicioActividad.getText().toString());
                        form.setFecha_Termino(FechaHoraTerminoActividad.getText().toString());
                        form.setTiempo_Total_Actividad(TiempoTotalActividad.getText().toString());
                        form.setCliente_Afectado(ClienteAfectado.getSelectedItem().toString());
                        form.setReferencia_Cliente(Referencia_Cliente);
                        form.setTipo_Cliente(EleccionCliente);
                        form.setLocalizacion_Falla(LocalizacionFalla.getText().toString());
                        form.setDescripcion_Materiales(DescribirMateriales.getText().toString());
                        form.setDescripcion_Trabajo(DescribirTrabajo.getText().toString());
                        form.setResolucion_Tabajo(ResolucionTrabajo.getText().toString());
                        form.setObservaciones(ObservacionTrabajo.getText().toString());
                        dbform.deleteformulario();
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

        Cargar_Preferencias();
        CargarDatosFormulario();

    }




    private void Cargar_Preferencias() {
        SharedPreferences sharedPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Operarios", "");
        Type type = new TypeToken<List<Operarios>>() {}.getType();
        SupervisoresT = new ArrayList<>();
        rutSupervisoresT = new ArrayList<>();
        TecnicoRepar = new ArrayList<>();
        rutTecnicoRepar = new ArrayList<>();
        TecnicoRepar2 = new ArrayList<>();
        rutTecnicoRepar2 = new ArrayList<>();
        ZonaMante = new ArrayList<>();
        Clientes = new ArrayList<>();

    //    SupervisoresT.add("");
    //    TecnicoRepar.add("");
   //    TecnicoRepar2.add("");
   //     ZonaMante.add("");
   //     Clientes.add("");

        try {
              List<Operarios> lista = gson.fromJson(json, type);
              for (int i = 0; i < lista.size(); i++) {
                  listop.add(i,lista.get(i).getRut());
                  String tipoop = String.valueOf(lista.get(i).getTipo());
                  switch (tipoop){
                      case "1":
                      rutSupervisoresT.add(lista.get(i).getRut());
                      SupervisoresT.add(lista.get(i).getNombre());
                      break;
                      case "2":
                      rutTecnicoRepar.add(lista.get(i).getRut());
                      TecnicoRepar.add(lista.get(i).getNombre());
                      break;
                      default:
                      rutTecnicoRepar2.add(lista.get(i).getRut());
                      TecnicoRepar2.add(lista.get(i).getNombre());
                      break;
                  }
              }
          } catch (Exception e) {
              e.printStackTrace();
              Toast.makeText(this," Error!!"+e.toString(),Toast.LENGTH_LONG).show();
        }
        if (SupervisoresT == null) {
            rutSupervisoresT = new ArrayList<>();
            SupervisoresT = new ArrayList<>();
        }
        if (TecnicoRepar == null) {
            rutTecnicoRepar = new ArrayList<>();
            TecnicoRepar = new ArrayList<>();
        }
        if (TecnicoRepar2 == null) {
            rutTecnicoRepar2 = new ArrayList<>();
            TecnicoRepar2 = new ArrayList<>();
        }
        ArrayAdapter<String> supervisores = new ArrayAdapter<>(FormularioRellenar.this, R.layout.spinner_item_formateado, SupervisoresT);
        SupervisorTurno.setAdapter(supervisores);
        ArrayAdapter<String> tecnicos = new ArrayAdapter<>(FormularioRellenar.this, R.layout.spinner_item_formateado, TecnicoRepar);
        TecnicoReparacion.setAdapter(tecnicos);
        ArrayAdapter<String> tecnicos2 = new ArrayAdapter<>(FormularioRellenar.this, R.layout.spinner_item_formateado, TecnicoRepar2);
        TecnicoReparacionOP.setAdapter(tecnicos2);

        String jsonzo = sharedPreferences.getString("Zonas", "");
        Type typezo = new TypeToken<List<Bodegas>>() {}.getType();
        try {
            List<Bodegas> lista = gson.fromJson(jsonzo, typezo);
            for (int i = 0; i < lista.size(); i++) {
                listzo.add(i,lista.get(i).getCodigo());
                ZonaMante.add(lista.get(i).getNombre());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this," Error!!"+e.toString(),Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> ListadoZona = new ArrayAdapter<String>(FormularioRellenar.this, R.layout.spinner_item_formateado, ZonaMante);
        ZonaMantenimiento.setAdapter(ListadoZona);

        String jsoncl = sharedPreferences.getString("Clientes", "");
        Type typecl = new TypeToken<List<Clientes>>() {}.getType();
        try {
            List<Clientes> lista = gson.fromJson(jsoncl, typecl);
            for (int i = 0; i < lista.size(); i++) {
                listcl.add(i,lista.get(i).getReferencia());
                listclcopia.add(i,lista.get(i).getReferencia());
                Clientes.add(lista.get(i).getNombre());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this," Error!!"+e.toString(),Toast.LENGTH_LONG).show();
        }

        ListadoClientes = new ArrayAdapter<String>(FormularioRellenar.this, R.layout.spinner_item_formateado, Clientes);
        ClienteAfectado.setAdapter(ListadoClientes);

        SupervisorTurno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer rutop = rutSupervisoresT.get(position);
                rutSup = String.valueOf(rutop);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        TecnicoReparacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer rutop = rutTecnicoRepar.get(position);
                rutTecRep1 = String.valueOf(rutop);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        TecnicoReparacionOP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer rutop = rutTecnicoRepar2.get(position);
                rutTecRep2 = String.valueOf(rutop);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ZonaMantenimiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer codzo = listzo.get(position);
                Zonaman = String.valueOf(codzo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ClienteAfectado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listcl.size()>0) {
                    Integer referenciacl = listcl.get(position);
                    Referencia_Cliente = String.valueOf(referenciacl);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void LimpiarCuadro() {
        TituloRecorrido.setText("");
        FechaRecorrido.setText("");
        SupervisorTurno.setSelection(0);
        TecnicoReparacion.setSelection(0);
        TecnicoReparacionOP.setSelection(0);
        ZonaMantenimiento.setSelection(0);
        FechaHoraInicioActividad.setText("");
        FechaHoraTerminoActividad.setText("");
        TiempoTotalActividad.setText("");
        ClienteAfectado.setSelection(0);
        GrupoTipoCliente.clearCheck();
        LocalizacionFalla.setText("");
        DescribirMateriales.setText("");
        DescribirTrabajo.setText("");
        ResolucionTrabajo.setText("");
        ObservacionTrabajo.setText("");
    }

    public void CargarDatosFormulario() {
        Formulario form = new Formulario();
        try {
            form = dbform.SelectFormulario();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String rut = "";
        int pos =0;
        registro_formulario=form.getRegistro();
        TituloRecorrido.setText(form.getTitulo_Form());
        FechaRecorrido.setText(form.getFecha_Form());
        rut = form.getSupervisor_Turno();
        rutSup = String.valueOf(rut);
        if (rut==null || rut.isEmpty()) {pos=-1;}
        else
        {
            pos = rutSupervisoresT.indexOf(Integer.valueOf(rut));
            if (pos==-1)
            {
                SupervisorTurno.setSelection(0);
                rutSup=rutSupervisoresT.get(0).toString();
            }
            else {
                SupervisorTurno.setSelection(pos);
            }
        };

        rut = form.getTecnico_Reparacion();
        rutTecRep1 = String.valueOf(rut);
        if (rut==null || rut.isEmpty()) {pos=-1;}
        else
        {
            pos = rutTecnicoRepar.indexOf(Integer.valueOf(rut));
            if (pos==-1)
            {
                TecnicoReparacion.setSelection(0);
                rutTecRep1=rutTecnicoRepar.get(0).toString();
            }
            else {
                TecnicoReparacion.setSelection(pos);
            }
        };

        rut = form.getTecnico_ReparacionOP();
        rutTecRep2 = String.valueOf(rut);
        if (rut==null || rut.isEmpty()) {pos=-1;}
        else
        {
            pos = rutTecnicoRepar.indexOf(Integer.valueOf(rut));
            if (pos==-1)
            {
                TecnicoReparacionOP.setSelection(0);
                rutTecRep2=rutTecnicoRepar2.get(0).toString();
            }
            else {
                TecnicoReparacionOP.setSelection(pos);
            }
        };

        TecnicoReparacionOP.setSelection(pos);
        rut = form.getZona_Mantenimiento();
        if (rut==null || rut.isEmpty()) {pos=-1;} else {pos = listzo.indexOf(Integer.valueOf(rut));}
        ZonaMantenimiento.setSelection(pos);
        FechaHoraInicioActividad.setText(form.getFecha_Inicio());
        FechaHoraTerminoActividad.setText(form.getFecha_Termino());
        TiempoTotalActividad.setText(form.getTiempo_Total_Actividad());
        rut = form.getReferencia_Cliente();
        if (rut==null || rut.isEmpty()) {pos=-1;} else {pos = listcl.indexOf(Integer.valueOf(rut));}
        ClienteAfectado.setSelection(pos);
        String tipocliente=form.getTipo_Cliente();
        if (tipocliente==null)
        {
            ((RadioButton)GrupoTipoCliente.findViewById(R.id.RdTroncal)).setChecked(true);
        }
        else
        {
            if (tipocliente.equals("Troncal"))
            {
                ((RadioButton)GrupoTipoCliente.findViewById(R.id.RdTroncal)).setChecked(true);
            }
            if (tipocliente.equals("Ultima Milla"))
            {
                ((RadioButton)GrupoTipoCliente.findViewById(R.id.RdUltimaMilla)).setChecked(true);
            }
        }
        LocalizacionFalla.setText(form.getLocalizacion_Falla());
        DescribirMateriales.setText(form.getDescripcion_Materiales());
        DescribirTrabajo.setText(form.getDescripcion_Trabajo());
        ResolucionTrabajo.setText(form.getResolucion_Tabajo());
        ObservacionTrabajo.setText(form.getObservaciones());
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

    public void ObtenerDiferencia(){
        int anio1 = 2022;
        int mes1 = 10;
        int d1= 7;
        int h1 = 12;
        int m1 = 44;
        int s1 = 0;
        long tt1 = 0;
        try {
            tt1 = new SimpleDateFormat("MM/dd/yyyy").parse(d1+"/"+mes1+"/"+anio1+""+h1+":"+m1+":"+s1+"").getTime() /1000;
        int anio2 = 2022;
        int mes2 = 10;
        int d2 = 7;
        int h2 = 12;
        int m2 = 43;
        int s2 = 0;
        long tt2 = new SimpleDateFormat("MM/DD/yyyy").parse(d2+"/"+mes2+"/"+anio2+""+h2+":" + m2 +":" +s2 + "").getTime() /1000;

        if(tt2 > tt1){
            Toast.makeText(this, "Fecha de termino es superior al de inicio ", Toast.LENGTH_SHORT).show();
        }else if (tt1 > tt2 ){
            Toast.makeText(this, "Fecha validas", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Fechas Iguales Rehacer fechas por favor", Toast.LENGTH_SHORT).show();
        }
    } catch (ParseException e) {
        e.printStackTrace();

    }
}

}

