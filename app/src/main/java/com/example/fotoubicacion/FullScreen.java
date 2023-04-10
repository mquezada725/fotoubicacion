package com.example.fotoubicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.example.fotoubicacion.DB.FotosDB;

public class FullScreen extends AppCompatActivity {
    Tools tools;
    EditText txt_comentario;
    EditText txt_ubicacion;
    Button btn_quitar;
    Button btn_guardar;
    EditText txt_idfoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        txt_comentario = findViewById(R.id.TxtComentario);
        txt_ubicacion = findViewById(R.id.TxtUbicacion);
        txt_idfoto = findViewById(R.id.idfoto);
        btn_quitar = findViewById(R.id.btnquitar);
        btn_guardar = findViewById(R.id.btnguardar);


        String filename = getIntent().getStringExtra("pathfoto");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(filename, options);
        ImageView imageview = (ImageView) this.findViewById(R.id.imagen);
        imageview.setImageBitmap(bitmap );

//        ImageView imageview = (ImageView) this.findViewById(R.id.imagen);
//        Bitmap b = BitmapFactory.decodeByteArray(
//                getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
//        imageview.setImageBitmap(b);
        txt_comentario.setText(getIntent().getStringExtra("comentario"));
        txt_ubicacion.setText(getIntent().getStringExtra("ubicacion"));
        txt_idfoto.setText(getIntent().getStringExtra("idfoto"));
        btn_quitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                          PopupMenu MenuVisual = new PopupMenu(FullScreen.this,view);
                          MenuVisual.inflate(R.menu.menuflotante);
                          MenuVisual.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                          @Override
                          public boolean onMenuItemClick(MenuItem menuItem) {
                                  switch (menuItem.getItemId()) {
                                      case R.id.Elimina_foto:
                                          FotosDB dbfoto = new FotosDB(FullScreen.this);
                                          try {
                                              String value= txt_idfoto.getText().toString();
                                              int id=Integer.parseInt(value);
                                              dbfoto.Abertura();
                                              boolean resultado=dbfoto.EliminarFoto(id);
                                              Intent i = new Intent(MainActivity.getContext(), EditarRecorrido.class);
                                              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                              startActivity(i);

                                              finish();
                                          } catch (Exception e) {
                                              e.printStackTrace();
                                          }
                                          dbfoto.Clausura();
                                          break;
                                      default:
                                          return false;
                                  }
                                 return false;
                             }

                         });
                         MenuVisual.show();
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FotosDB dbfoto = new FotosDB(FullScreen.this);
                try {
                    String value= txt_idfoto.getText().toString();
                    String u= txt_ubicacion.getText().toString();
                    String c= txt_comentario.getText().toString();
                    int id=Integer.parseInt(value);
                    dbfoto.Abertura();
                    boolean resultado=dbfoto.ModificarComentarios(id,u,c);
                    Intent i = new Intent(MainActivity.getContext(), EditarRecorrido.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dbfoto.Clausura();
                finish();
            }
        });

    }
}