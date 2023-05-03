package com.jhon.gen_dorado_oficial.Objetos;

import com.google.type.Date;
import com.google.type.DateTime;

public class Medicamentos {
    String medicamento; //nombre medcamento
    DateTime horario; //cada cuanto se debe tomar
    int dosis; //cuantas debe tomar
    Date fechainicio; //cuando empezo
    DateTime horainicio; //a que horas empezo

    public Medicamentos() {

    }

    public Medicamentos(String medicamento, DateTime horario, int dosis, Date fechainicio, DateTime horainicio) {
        this.medicamento = medicamento;
        this.horario = horario;
        this.dosis = dosis;
        this.fechainicio = fechainicio;
        this.horainicio = horainicio;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public DateTime getHorario() {
        return horario;
    }

    public void setHorario(DateTime horario) {
        this.horario = horario;
    }

    public int getDosis() {
        return dosis;
    }

    public void setDosis(int dosis) {
        this.dosis = dosis;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public DateTime getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(DateTime horainicio) {
        this.horainicio = horainicio;
    }
}
