package com.example.fotoubicacion.Models;

public class Fotos {

    int RegistroFoto;
    String PathImagen;
    byte[] ImagenReal;
    String UbicacionSolicitada;
    String ComentarioFoto;

    public int getRegistroFoto() {
        return RegistroFoto;
    }

    public void setRegistroFoto(int registroFoto) {
        RegistroFoto = registroFoto;
    }

    public String getPathImagen() {return PathImagen;}

    public void setPathImagen(String pathimagen) {
        PathImagen = pathimagen;
    }

    public void setImagenReal(byte[] imagenReal) {
        ImagenReal = imagenReal;
    }

    public byte[] getImagenReal() {return ImagenReal;}

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
