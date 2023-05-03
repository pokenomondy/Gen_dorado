package com.jhon.gen_dorado_oficial.Objetos;

public class Familiares {
    String cedula;
    String nombrefamiliar;
    String apodofamiliar;
    String uid;

    public Familiares() {
    }

    public Familiares(String cedula, String nombrefamiliar, String apodofamiliar, String uid) {
        this.cedula = cedula;
        this.nombrefamiliar = nombrefamiliar;
        this.apodofamiliar = apodofamiliar;
        this.uid = uid;

    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombrefamiliar() {
        return nombrefamiliar;
    }

    public void setNombrefamiliar(String nombrefamiliar) {
        this.nombrefamiliar = nombrefamiliar;
    }

    public String getApodofamiliar() {
        return apodofamiliar;
    }

    public void setApodofamiliar(String apodofamiliar) {
        this.apodofamiliar = apodofamiliar;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }

}
