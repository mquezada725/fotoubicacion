package com.example.fotoubicacion.Entidades;

public class ListadoFoto {


    private int ID_foto;
    private byte[] Foto_Tomada;
    private String UbicacionFoto;
    private String Comentario;

    public ListadoFoto(int iDtomar, byte[] imagenFoto, String ubicacionEscrita, String comentarioOp) {
        ID_foto = iDtomar;
        Foto_Tomada = imagenFoto;
        UbicacionFoto = ubicacionEscrita;
        Comentario = comentarioOp;
    }




    public int getID_foto() {
        return ID_foto;
    }

    public void setID_foto(int ID_foto) {
        this.ID_foto = ID_foto;
    }

    public String getUbicacionFoto() {
        return UbicacionFoto;
    }

    public void setUbicacionFoto(String ubicacionFoto) {
        UbicacionFoto = ubicacionFoto;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }


    public byte[] getFoto_Tomada() {
        return Foto_Tomada;
    }

    public void setFoto_Tomada(String foto_tomada) {
        Foto_Tomada = Foto_Tomada;
    }
}
