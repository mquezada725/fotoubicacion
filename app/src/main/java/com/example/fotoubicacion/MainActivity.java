package com.example.fotoubicacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button PasarFotos, PasarEditar;
    Button PasarFormulario;
    Button CrearDBnuevoRecorrido;
    Button Sincronizar;
    private Transition transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.fondopantalla);
        getSupportActionBar().hide();

        PasarFotos = findViewById(R.id.BtnNuevaFoto);
        PasarFormulario = findViewById(R.id.BtnFormulario);
        PasarEditar = findViewById(R.id.BtnEditarListado);
        CrearDBnuevoRecorrido = findViewById(R.id.BtnRecorridoNuevo);
        Sincronizar = findViewById(R.id.btnSincronizacion);


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
                builder.setMessage("Generar Nuevo Recorrido ?")
                        .setTitle("Mensaje Confirmación");

                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Recorrido Nuevo Creado", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Solicitud Rechazada Por Usuario", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });



        Sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder Build = new AlertDialog.Builder(MainActivity.this);
                Build.setMessage("Desea Enviar Datos A Base de Datos?")
                        .setTitle("Confirmacioón");
                Build.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Enviado Datos a la Nube", Toast.LENGTH_SHORT).show();
                    }
                });
                Build.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Solicitud Rechazada Por Usuario", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = Build.create();
                alertDialog.show();
            }
        });
    }
}