package com.example.fotoubicacion.Entidades;

public class FotosModels {
    private int ID_foto;
    private String Path_Foto;
    private byte[] Imagen_Real;
    private String UbicacionFoto;
    private String Comentario;

    public FotosModels(int iD, String pathFoto, byte[] imagenReal, String ubicacion, String comentario) {
        ID_foto = iD;
        Path_Foto = pathFoto;
        Imagen_Real = imagenReal;
        UbicacionFoto = ubicacion;
        Comentario = comentario;
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

    public String getPath_Foto() {return Path_Foto;}

    public void setPath_Foto(String path_Foto) {
        Path_Foto = path_Foto;
    }

    public byte[] getImagen_Real() {
        return Imagen_Real;
    }

    public void setImagen_Real(byte[] imagen_Real) {
        Imagen_Real = imagen_Real;
    }
}
