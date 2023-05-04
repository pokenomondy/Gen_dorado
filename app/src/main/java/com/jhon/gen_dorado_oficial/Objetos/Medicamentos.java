package com.jhon.gen_dorado_oficial.Objetos;

import com.google.firebase.database.DataSnapshot;
import com.google.type.DateTime;

import java.util.Date;

public class Medicamentos {
    String medicamento; //nombre medicamento
    String horario; //cada cuanto se debe tomar
    int dosis; //cuantas debe tomar
    String calcsigmedicamento;
    java.util.Date fechainicio; //cuando empezo

    String intervaloaplicacion;


    public Medicamentos() {
    }

    public Medicamentos(String medicamento, String horario, int dosis, String calcsigmedicamento, Date fechainicio, String intervaloaplicacion) {
        this.medicamento = medicamento;
        this.horario = horario;
        this.dosis = dosis;
        this.calcsigmedicamento = calcsigmedicamento;
        this.fechainicio = fechainicio;
        this.intervaloaplicacion = intervaloaplicacion;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getDosis() {
        return dosis;
    }

    public void setDosis(int dosis) {
        this.dosis = dosis;
    }

    public String getCalcsigmedicamento() {
        return calcsigmedicamento;
    }

    public void setCalcsigmedicamento(String calcsigmedicamento) {
        this.calcsigmedicamento = calcsigmedicamento;
    }

    public java.util.Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getIntervaloaplicacion() {
        return intervaloaplicacion;
    }

    public void setIntervaloaplicacion(String intervaloaplicacion) {
        this.intervaloaplicacion = intervaloaplicacion;
    }

}
