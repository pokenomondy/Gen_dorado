package com.jhon.gen_dorado_oficial.Objetos;

import com.google.type.Date;
import com.google.type.DateTime;

public class Medicamentos {
    String medicamento; //nombre medcamento
    String horario; //cada cuanto se debe tomar
    String dosis; //cuantas debe tomar
    String calcsigmedicamento;
    String fechainicio; //cuando empezo
    String horainicio; //a que horas empezo


    public Medicamentos() {
    }

    public Medicamentos(String medicamento, String horario, String dosis, String calcsigmedicamento, String fechainicio, String horainicio) {
        this.medicamento = medicamento;
        this.horario = horario;
        this.dosis = dosis;
        this.calcsigmedicamento = calcsigmedicamento;
        this.fechainicio = fechainicio;
        this.horainicio = horainicio;
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

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getCalcsigmedicamento() {
        return calcsigmedicamento;
    }

    public void setCalcsigmedicamento(String calcsigmedicamento) {
        this.calcsigmedicamento = calcsigmedicamento;
    }

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }
}
