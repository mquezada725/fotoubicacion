package com.example.fotoubicacion.Adaptadores;

import static com.example.fotoubicacion.DB.DBHelper.CREAR_TABLA_FOTOS;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fotoubicacion.DB.DBHelper;
import com.example.fotoubicacion.DB.FotosDB;
import com.example.fotoubicacion.EditarRecorrido;
import com.example.fotoubicacion.Entidades.ListadoFoto;
import com.example.fotoubicacion.R;

import java.util.ArrayList;

public class AdaptadorFotos extends RecyclerView.Adapter<AdaptadorFotos.viewHolder> {
    Context Contenido;
    int Dato_simple;
    ArrayList<ListadoFoto> ModeloListado;
    SQLiteDatabase sqLiteDatabase;



    //Clase Constructor

    public AdaptadorFotos(Context contenido, int dato_simple, ArrayList<ListadoFoto> modeloListado, SQLiteDatabase sqLiteDatabase) {
        Contenido = contenido;
        Dato_simple = dato_simple;
        ModeloListado = modeloListado;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public AdaptadorFotos.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater Layoutflater = LayoutInflater.from(Contenido);
        View view = Layoutflater.inflate(R.layout.muestrafotodato,null);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorFotos.viewHolder holder, int position) {
        final ListadoFoto listarfotos = ModeloListado.get(position);
        byte[] imagen = listarfotos.getFoto_Tomada();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagen,0,imagen.length);
        holder.ImgCargarDatoFoto.setImageBitmap(bitmap);
        holder.UbicacionFotografia.setText(listarfotos.getUbicacionFoto());
        holder.ComentarioFotografia.setText(listarfotos.getComentario());

        //Menu De Opciones
        holder.FlowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu MenuVisual = new PopupMenu(Contenido,holder.FlowMenu);
                MenuVisual.inflate(R.menu.menuflotante);
                MenuVisual.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                       switch (menuItem.getItemId()){
                           case R.id.Elimina_foto:
                               DBHelper dbHelper = new DBHelper(Contenido);
                               sqLiteDatabase = dbHelper.getReadableDatabase();
                               long eliminarFoto = sqLiteDatabase.delete(CREAR_TABLA_FOTOS,"ID_FOTO = " + listarfotos.getID_foto(),null  );
                                if(eliminarFoto != -1){
                                    Toast.makeText(Contenido,"Foto Eliminada",Toast.LENGTH_SHORT).show();
                                    ModeloListado.remove(position);
                                    notifyDataSetChanged();
                                }
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
    }

    @Override
    public int getItemCount() {
        return ModeloListado.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView ImgCargarDatoFoto;
        TextView UbicacionFotografia;
        TextView ComentarioFotografia;
        ImageButton FlowMenu;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ImgCargarDatoFoto = itemView.findViewById(R.id.ImgCargarFoto);
            UbicacionFotografia = itemView.findViewById(R.id.TxtUbicacion);
            ComentarioFotografia = itemView.findViewById(R.id.TxtComentario);
            FlowMenu = itemView.findViewById(R.id.BtnFlowmenu);

        }
    }
}
