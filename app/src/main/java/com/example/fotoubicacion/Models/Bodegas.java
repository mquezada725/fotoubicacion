package com.example.fotoubicacion.Models;

public class Bodegas {
    public byte Empresa;

    public byte Codigo;

    public String Nombre;

    public int Referencia;

    public byte getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(byte empresa) {
        Empresa = empresa;
    }

    public byte getCodigo() {
        return Codigo;
    }

    public void setCodigo(byte codigo) {
        Codigo = codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getReferencia() {
        return Referencia;
    }

    public void setReferencia(int referencia) {
        Referencia = referencia;
    }
}
