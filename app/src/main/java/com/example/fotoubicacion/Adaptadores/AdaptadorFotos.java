package com.example.fotoubicacion.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fotoubicacion.Entidades.FotosModels;
import com.example.fotoubicacion.FullScreen;
import com.example.fotoubicacion.R;
import com.example.fotoubicacion.Tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class AdaptadorFotos extends RecyclerView.Adapter<AdaptadorFotos.viewHolder> {
    Context contexto;
    ArrayList<FotosModels> Arrayfotos;
    Tools tools;

    public AdaptadorFotos(Context contex, int rid,ArrayList<FotosModels> arrayfotos) {
        this.contexto = contex;
        this.Arrayfotos = arrayfotos;
    }

    @NonNull
    @Override
    public AdaptadorFotos.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater Layoutflater = LayoutInflater.from(contexto);
        View view = Layoutflater.inflate(R.layout.muestrafotodato,null);
        return new viewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    //carga de listado de fotos y opciones
    public void onBindViewHolder(@NonNull AdaptadorFotos.viewHolder holder, int position) {
        final FotosModels listarfotos = Arrayfotos.get(position);
        byte[] by = listarfotos.getImagen_Real();
        Bitmap bitmap = BitmapFactory.decodeByteArray(by,0,by.length);
        holder.ImgFotoReducida.setImageBitmap(bitmap );
        holder.UbicacionFotografia.setText(listarfotos.getUbicacionFoto());
        holder.ComentarioFotografia.setText(listarfotos.getComentario());
        holder.ComentarioFotografia.setText(listarfotos.getComentario());
        holder.IdFoto.setText(String.valueOf(listarfotos.getID_foto()));
        String xx=String.valueOf(listarfotos.getPath_Foto());
        holder.PathFoto.setText(String.valueOf(listarfotos.getPath_Foto()));
     }

    @Override
    public int getItemCount() {
        return Arrayfotos.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ImgFotoReducida;
        TextView UbicacionFotografia;
        TextView ComentarioFotografia;
        TextView IdFoto;
        TextView PathFoto;
        ImageButton FlowMenu;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ImgFotoReducida = itemView.findViewById(R.id.ImgFotoReducida);
            UbicacionFotografia = itemView.findViewById(R.id.TxtUbicacion);
            ComentarioFotografia = itemView.findViewById(R.id.TxtComentario);
            IdFoto = itemView.findViewById(R.id.idfoto);
            PathFoto = itemView.findViewById(R.id.pathfoto);
            FlowMenu = itemView.findViewById(R.id.BtnFlowmenu);
            contexto = itemView.getContext();
            itemView.setOnClickListener(this);
        }
             @Override
             public void onClick(View v) {
                 int pos = getAdapterPosition();
                 Intent i = new Intent(contexto, FullScreen.class);
                 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 i.putExtra("pathfoto", PathFoto.getText());
                 i.putExtra("ubicacion", UbicacionFotografia.getText());
                 i.putExtra("comentario", ComentarioFotografia.getText());
                 i.putExtra("idfoto", IdFoto.getText());
                 contexto.startActivity(i);
           }
    }
}
