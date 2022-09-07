package com.example.fotoubicacion;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.DiffUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fotoubicacion.DB.DBHelper;
import com.example.fotoubicacion.DB.FormularioDB;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FormularioRellenar extends AppCompatActivity {

    SQLiteDatabase sqLiteDb;
    FormularioDB dbform;
    DBHelper DB;
    EditText TituloRecorrido, FechaRecorrido, LocalizacionFalla, DistanciaOptica, DescribirTrabajo, ResolucionTrabajo, ObservacionTrabajo, DescribirMateriales;
    EditText Eventos;
    EditText Latitud;
    EditText Longitud;
    EditText FechaHoraInicioActividad;
    EditText FechaHoraTerminoActividad;
    EditText TiempoTotalActividad;
    RadioButton EventoSi, EventoNo, PerdidaServicioSi, PerdidaServicioNo, TipoClienteTroncal, TipoClienteUltimaMilla, RegistroAntesSi, RegistroAntesNo, RegistroFinalSi, RegistroFinalNo, RegistroMedicionAntesSi, RegistroMedicionAntesNo, RegistroMedicionAntesNoAplica, RegistroMedicionFinalSi, RegistroMedicionFinalNo, RegistroMedicionFinalNoAplica, CerrarModificacionSi, CerrarModificacionNo;
    RadioGroup GrupoEventos, GrupoPerdidaServicio, GrupoTipoCliente, GrupoRegistroFotoAntes, GrupoRegistroFotoFinal, GrupoRegistroMedicionAntes, GrupoRegistroMedicionFinal, GrupoCerrarModificacion;
    Spinner SupervisorTurno, TecnicoReparacion, TecnicoReparacion2, ZonaMantenimiento, ClienteAfectado;
    Button IndicarFecha, AgregarArchivo, GuardarRecorrido;

    //Seleccion RadioGroup
    //Creamos Variables para tomar Fecha

    int Hora, Minutos, HoraT, MinutosT, HoraCompleto,MinutosCompleto;
    int Dia, Mes, Years, DiaT, MesT, YearsT, DiaI,MesI,YearsI;

    //Establecemos Comunicacion Con Sqlite
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_rellenar);
        getSupportActionBar().hide();
        //Metodo Para Agregar Archivo
        dbform = new FormularioDB(FormularioRellenar.this);
        //Establecemos Comunicacion Con los datos En la Parte Grafica Definida
        // Tipo EditText
        TituloRecorrido = findViewById(R.id.TxtTitulo);
        FechaRecorrido = findViewById(R.id.TxtFechaObtenido);
        LocalizacionFalla = findViewById(R.id.TxtLocalizacionFalla);
        DistanciaOptica = findViewById(R.id.TxtDistanciaOptica);
        DescribirMateriales = findViewById(R.id.TxtDecribirMateriales);
        DescribirTrabajo = findViewById(R.id.TxtDescribirTrabajo);
        ResolucionTrabajo = findViewById(R.id.TxtResolutivoTrabajo);
        ObservacionTrabajo = findViewById(R.id.TxtObservacionesTexto);
        FechaHoraInicioActividad = findViewById(R.id.TxtHoraInicioActividad);
        FechaHoraTerminoActividad = findViewById(R.id.TxtHoraTerminoActividad);
        TiempoTotalActividad = findViewById(R.id.TxtTotalActividad);

        // Tipo Botones
        AgregarArchivo = findViewById(R.id.BtnAdjuntarArchivo);
        GuardarRecorrido = findViewById(R.id.BtnGuardarRecorrido);

        //Tipo Numero
        Eventos = findViewById(R.id.TxtNumeroEvento);
        Latitud = findViewById(R.id.TxtLatitudObtener);
        Longitud = findViewById(R.id.TxtLongitudObtener);

        //Tipo Radio Button
        EventoSi = findViewById(R.id.RdEventoSi);
        EventoNo = findViewById(R.id.RdEventoNo);
        PerdidaServicioSi = findViewById(R.id.RdPerdidaSi);
        PerdidaServicioNo = findViewById(R.id.RdPerdidaNo);
        TipoClienteTroncal = findViewById(R.id.RdTroncal);
        TipoClienteUltimaMilla = findViewById(R.id.RdUltimaMilla);
        RegistroAntesSi = findViewById(R.id.RdFotoAntesSi);
        RegistroAntesNo = findViewById(R.id.RdFotoAntesNo);
        RegistroFinalSi = findViewById(R.id.RdRegistroFotoFinalSi);
        RegistroFinalNo = findViewById(R.id.RdRegistroFotoFinalNo);
        RegistroMedicionAntesSi = findViewById(R.id.RdMedicionAntesSi);
        RegistroMedicionAntesNo = findViewById(R.id.RdMedicionAntesNo);
        RegistroMedicionAntesNoAplica = findViewById(R.id.RdRegistroMedicionAntesNoAplica);
        RegistroMedicionFinalSi = findViewById(R.id.RdMedicionFinalSi);
        RegistroMedicionFinalNo = findViewById(R.id.RdMedicionFinalNo);
        RegistroMedicionFinalNoAplica = findViewById(R.id.RdMedicionFinalNoAplica);
        CerrarModificacionSi = findViewById(R.id.RdCerrarModificacionSi);
        CerrarModificacionNo = findViewById(R.id.RdCerrarModificacionNo);

        //Tipo Radio Group
        GrupoEventos = findViewById(R.id.RgEventos);
        GrupoPerdidaServicio = findViewById(R.id.RgPerdidaServicio);
        GrupoTipoCliente = findViewById(R.id.RgTipoCliente);
        GrupoRegistroFotoAntes = findViewById(R.id.RgFotoAntes);
        GrupoRegistroFotoFinal = findViewById(R.id.RgFotoFinal);
        GrupoRegistroMedicionAntes = findViewById(R.id.RgMedicionAntes);
        GrupoRegistroMedicionFinal = findViewById(R.id.RgMedicionFinal);
        GrupoCerrarModificacion = findViewById(R.id.RgCerrarModificacion);

        //Tipo Spinner
        SupervisorTurno = findViewById(R.id.SpSupervisor);
        TecnicoReparacion = findViewById(R.id.SpTecnicoReparacion);
        TecnicoReparacion2 = findViewById(R.id.SpTecnicoReparacionOpcional);
        ZonaMantenimiento = findViewById(R.id.SpZonaMantenimiento);
        ClienteAfectado = findViewById(R.id.SpClienteAfectado);

        //Tipo Time


        //Llamar Metodos
        ListadoSupervisorTurno();
        ListadoTecnicoReparacion();
        ListadoTecnicoReparacionOpcional();
        ListadoZonaMantenimiento();
        ListadoNombre();

        //Metodo Calendario Para Tiempo


        //Creamos Obtencion Fecha

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


        //Mensajes Para Radio Button

        GrupoEventos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ChecarEvento) {
                if (ChecarEvento == R.id.RdEventoSi) {
                    Toast.makeText(FormularioRellenar.this, "Asociado A Evento = Si", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ChecarEvento == R.id.RdEventoNo) {
                    Toast.makeText(FormularioRellenar.this, "Asociado A Evento = No", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        GrupoPerdidaServicio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ChecarServicio) {
                if (ChecarServicio == R.id.RdPerdidaSi) {
                    Toast.makeText(FormularioRellenar.this, "Perdida De Servicio = Si", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ChecarServicio == R.id.RdPerdidaNo) {
                    Toast.makeText(FormularioRellenar.this, "Perdida De Servicio = No", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

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

        GrupoRegistroFotoAntes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ChecarFotoAntes) {
                if (ChecarFotoAntes == R.id.RdFotoAntesSi) {
                    Toast.makeText(FormularioRellenar.this, "Registro Foto Antes = Si", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ChecarFotoAntes == R.id.RdFotoAntesNo) {
                    Toast.makeText(FormularioRellenar.this, "Registro Foto Antes = No", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        GrupoRegistroFotoFinal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ChecarFotoFinal) {
                if (ChecarFotoFinal == R.id.RdRegistroFotoFinalSi) {
                    Toast.makeText(FormularioRellenar.this, "Registro Foto al Final = Si", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ChecarFotoFinal == R.id.RdRegistroFotoFinalNo) {
                    Toast.makeText(FormularioRellenar.this, "Registro Foto al Final = No", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        GrupoRegistroMedicionAntes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ChecarMedicionAntes) {
                if (ChecarMedicionAntes == R.id.RdMedicionAntesSi) {
                    Toast.makeText(FormularioRellenar.this, "Registro De Medición Antes = Si", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ChecarMedicionAntes == R.id.RdMedicionAntesNo) {
                    Toast.makeText(FormularioRellenar.this, "Registro De Medición Antes = No", Toast.LENGTH_SHORT).show();
                } else if (ChecarMedicionAntes == R.id.RdRegistroMedicionAntesNoAplica) {
                    Toast.makeText(FormularioRellenar.this, "Registro De Medición Antes = No Aplica", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        GrupoRegistroMedicionFinal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ChecarMedicionFinal) {
                if (ChecarMedicionFinal == R.id.RdMedicionFinalSi) {
                    Toast.makeText(FormularioRellenar.this, "Registro Medición Al Final = Si", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ChecarMedicionFinal == R.id.RdMedicionFinalNo) {
                    Toast.makeText(FormularioRellenar.this, "Registro Medición Al Final = No", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ChecarMedicionFinal == R.id.RdMedicionFinalNoAplica) {
                    Toast.makeText(FormularioRellenar.this, "Registro Medición Al Final = No Aplica", Toast.LENGTH_SHORT).show();
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

                final String ValidacionLetras = Latitud.getText().toString();
                final String Validacionletas2 = Longitud.getText().toString();
                Intent IntentoGuardarRecorrido = new Intent(FormularioRellenar.this, MainActivity.class);

                if (TituloRecorrido.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Titulo Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (FechaRecorrido.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Fecha Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GrupoEventos.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(FormularioRellenar.this, "Campo Asociado a Eventos Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Eventos.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo N° Evento Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GrupoPerdidaServicio.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(FormularioRellenar.this, "Campo Perdida Servicio Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (SupervisorTurno.getSelectedItemPosition() == 0) {
                    Toast.makeText(FormularioRellenar.this, "Campo Supervisor Vacio", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TecnicoReparacion.getSelectedItemPosition() == 0) {
                    Toast.makeText(FormularioRellenar.this, "Campo Tecnico Reparación Vacio", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ZonaMantenimiento.getSelectedItemPosition() == 0) {
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
                if (DistanciaOptica.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Distancia Óptica Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Latitud.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Latitud Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Longitud.getText().toString().isEmpty()) {
                    Toast.makeText(FormularioRellenar.this, "Campo Longitud Vació", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GrupoRegistroFotoAntes.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(FormularioRellenar.this, "Seleccione Opción En Foto Antes", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GrupoRegistroFotoFinal.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(FormularioRellenar.this, "Seleccione Opción En Foto Final", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GrupoRegistroMedicionAntes.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(FormularioRellenar.this, "Seleccione Una Opción En Medición Antes", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GrupoRegistroMedicionFinal.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(FormularioRellenar.this, "Seleccione Una Opción En Medición Final", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GrupoCerrarModificacion.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(FormularioRellenar.this, "Seleccione Una Opción En Cerrar Modificación", Toast.LENGTH_SHORT).show();
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

                    //FormularioDB dbFormulario = new FormularioDB(FormularioRellenar.this);
                    try {
                        dbform.InsercionFormulario(
                                TituloRecorrido.getText().toString(),
                                FechaRecorrido.getText().toString(),
                                String.valueOf(GrupoEventos.getCheckedRadioButtonId()),
                                SupervisorTurno.toString(),
                                TecnicoReparacion.toString(),
                                TecnicoReparacion2.toString(),
                                ZonaMantenimiento.toString(),
                                FechaHoraInicioActividad.toString(),
                                FechaHoraTerminoActividad.toString(),
                                TiempoTotalActividad.toString(),
                                ClienteAfectado.toString(),
                                String.valueOf(GrupoTipoCliente.getCheckedRadioButtonId()),
                                LocalizacionFalla.getText().toString(),
                                DistanciaOptica.getText().toString(),
                                Latitud.getText().toString(),
                                Longitud.getText().toString(),
                                String.valueOf(GrupoRegistroFotoAntes.getCheckedRadioButtonId()),
                                String.valueOf(GrupoRegistroFotoFinal.getCheckedRadioButtonId()),
                                String.valueOf(GrupoRegistroMedicionAntes.getCheckedRadioButtonId()),
                                String.valueOf(GrupoRegistroMedicionFinal.getCheckedRadioButtonId()),
                                String.valueOf(GrupoCerrarModificacion.getCheckedRadioButtonId()),
                                DescribirMateriales.getText().toString(),
                                DescribirTrabajo.getText().toString(),
                                ResolucionTrabajo.getText().toString(),
                                ObservacionTrabajo.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    startActivity(IntentoGuardarRecorrido);
                    Toast.makeText(FormularioRellenar.this, "Registro De Recorrido Guardado", Toast.LENGTH_SHORT).show();
                    LimpiarCuadro();
                }

            }
        });

        //Agregar Archivo


    }


    //Crear Metodos

    public void ListadoSupervisorTurno() {
        ArrayList<String> SupervisoresT = new ArrayList<>();
        SupervisoresT.add("Supervisor");
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
        TecnicoRepar.add("Tecnico De Reparación");
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
        TecnicoReparOpc.add("Tecnico Reparación 2 \n(Opcional)");
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
        ZonaMante.add("Zona De Mantenimiento");
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
        ListarClientes.add("Cliente Afectado \nNombre Del Cliente ");
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
        GrupoEventos.setSelected(false);
        Eventos.setText("");
        GrupoPerdidaServicio.clearCheck();
        SupervisorTurno.setSelection(0);
        TecnicoReparacion.setSelection(0);
        TecnicoReparacion2.setSelection(0);
        ZonaMantenimiento.setSelection(0);
        FechaHoraInicioActividad.setText("");
        FechaHoraTerminoActividad.setText("");
        ClienteAfectado.setSelection(0);
        GrupoTipoCliente.clearCheck();
        LocalizacionFalla.setText("");
        DistanciaOptica.setText("");
        Latitud.setText("");
        Longitud.setText("");
        GrupoRegistroFotoAntes.clearCheck();
        GrupoRegistroFotoFinal.clearCheck();
        GrupoRegistroMedicionAntes.clearCheck();
        GrupoRegistroMedicionFinal.clearCheck();
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
                Date dateMax = simpleDateFormat.parse("24:00");
                Date dateMin = simpleDateFormat.parse("00:00");
                difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
            }
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            Log.i("log_tag","Hours: " + hours + ", Mins: " + min);
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }



}



