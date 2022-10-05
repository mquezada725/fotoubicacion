package com.example.fotoubicacion.Models;

import android.graphics.drawable.Drawable;

public class Fotos {

    int RegistroFoto;
    String ImagenTomada;
    String UbicacionSolicitada;
    String ComentarioFoto;

    public int getRegistroFoto() {
        return RegistroFoto;
    }

    public void setRegistroFoto(int registroFoto) {
        RegistroFoto = registroFoto;
    }

    public String getImagenTomada() {
        return ImagenTomada;
    }

    public void setImagenTomada(String imagenTomada) {
        ImagenTomada = imagenTomada;
    }

    public String getUbicacionSolicitada() {
        return UbicacionSolicitada;
    }

    public void setUbicacionSolicitada(String ubicacionSolicitada) {
        UbicacionSolicitada = ubicacionSolicitada;
    }

    public String getComentarioFoto() {
        return ComentarioFoto;
    }

    public void setComentarioFoto(String comentarioFoto) {
        ComentarioFoto = comentarioFoto;
    }



}
