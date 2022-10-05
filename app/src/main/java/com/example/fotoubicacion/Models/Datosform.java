package com.example.fotoubicacion.Models;

import java.util.List;

public class Datosform {


    public List<Operarios> Operarios ;

    public List<Bodegas> Bodegas;


    public List<Operarios> getOperarios() {
        return Operarios;
    }

    public void setOperarios(List<com.example.fotoubicacion.Models.Operarios> operarios) {
        Operarios = operarios;
    }

    public class Bodegas

    {

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


/*
    public class Operarios

    {

        public int Empresa;

        public int Rut;

        public String Dv ;

        public String Nombre;

        public int Especialidad ;

        public int Valorhh ;

        public int getEmpresa() {
            return Empresa;
        }

        public void setEmpresa(int empresa) {
            Empresa = empresa;
        }

        public int getRut() {
            return Rut;
        }

        public void setRut(int rut) {
            Rut = rut;
        }

        public String getDv() {
            return Dv;
        }

        public void setDv(String dv) {
            Dv = dv;
        }

        public String getNombre() {
            return Nombre;
        }

        public void setNombre(String nombre) {
            Nombre = nombre;
        }

        public int getEspecialidad() {
            return Especialidad;
        }

        public void setEspecialidad(int especialidad) {
            Especialidad = especialidad;
        }

        public int getValorhh() {
            return Valorhh;
        }

        public void setValorhh(int valorhh) {
            Valorhh = valorhh;
        }
    }

 */
}
