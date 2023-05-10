package com.jhon.gen_dorado_oficial.Objetos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Historialmedicamento {
    int numdosis;
    String horatomada;
    String calculotomado;

    public Historialmedicamento() {
    }

    public Historialmedicamento(int numdosis, String horatomada, String calculotomado) {
        this.numdosis = numdosis;
        this.horatomada = horatomada;
        this.calculotomado = calculotomado;
    }

    public int getNumdosis() {
        String numtext = String.valueOf(this.numdosis);
        return numdosis;
    }

    public void setNumdosis(int numdosis) {
        this.numdosis = numdosis;
    }

    public String getHoratomada() {
        return horatomada;
    }

    public void setHoratomada(String horatomada) {
        this.horatomada = horatomada;
    }

    public String getCalculotomado() {
        //Calculo tomado
        return calculotomado;
    }

    public void setCalculotomado(String calculotomado) {
        this.calculotomado = calculotomado;
    }

}
