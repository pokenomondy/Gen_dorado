package com.jhon.gen_dorado_oficial.Objetos;

import com.google.firebase.database.DataSnapshot;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.logging.SimpleFormatter;

public class Medicamentos {
    String medicamento; //nombre medicamento
    int dosis; //cuantas debe tomar
    Map<String, Integer> fechainicio; //cuando empezo
    String intervaloaplicacion;


    public Medicamentos() {
    }

    public Medicamentos(String medicamento, int dosis, Map<String, Integer> fechainicio, String intervaloaplicacion) {
        this.medicamento = medicamento;
        this.dosis = dosis;
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
        Date fecha = obtenerFechaInicio();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.HOUR, Integer.parseInt(this.intervaloaplicacion));
        int hora_int = calendar.get(Calendar.HOUR_OF_DAY);
        int min_int = calendar.get(Calendar.MINUTE);
        String hora = "";
        String minuto = "";
        if(min_int < 10){
            minuto = "0" + Integer.toString(min_int);
        }else{
            minuto = Integer.toString(min_int);
        }
        if(hora_int>=12){
            if (hora_int % 12<10){
                hora = "0" + Integer.toString(hora_int % 12) + ":" + minuto + " p.m.";
            }else {
                hora = Integer.toString(hora_int % 12) + ":" + minuto + " p.m.";
            }
        }else{
            if(hora_int<10){
                hora = "0" +Integer.toString(hora_int) + ":" + minuto + " a.m.";
            }else{
                hora = Integer.toString(hora_int) + ":" + minuto + " a.m.";
            }
        }
        return hora;
    }

    public int getDosis() {
        return dosis;
    }

    public void setDosis(int dosis) {
        this.dosis = dosis;
    }

    public java.util.Date obtenerFechaInicio(){

        int dia_init = (int) this.fechainicio.get("dia");
        int año_init = (int) this.fechainicio.get("año");
        int mes_init = (int) this.fechainicio.get("mes");
        int hora_init = (int) this.fechainicio.get("hora");
        int min_init = (int) this.fechainicio.get("minuto");
        int sec_init = (int) this.fechainicio.get("segundo");
        Date inicio = new Date(año_init,mes_init,dia_init,hora_init,min_init,sec_init);

        return inicio;
    }

    public String getCalcsigmedicamento() {

        Date inicio = obtenerFechaInicio();
        int intervalo_medicamento = Integer.parseInt(this.intervaloaplicacion);

        Calendar fecha_inicio = Calendar.getInstance();
        fecha_inicio.setTime(inicio);

        fecha_inicio.add(Calendar.HOUR, intervalo_medicamento);

        Date fecha_inicio_add = fecha_inicio.getTime();
        Date fecha_ahora = new Date();

       //MILISEGUNDOS
        long diferencia = fecha_inicio_add.getTime() - fecha_ahora.getTime();
        //HORAS
        long diferencia_minutos
                = (diferencia
                / (1000 * 60))
                % 60;

        long diferencia_horas
                = (diferencia
                / (1000 * 60 * 60))
                % 24;

        return "Siguiente en " +  diferencia_horas + " horas y " + diferencia_minutos + " minutos";
    }


    public Map<String, Integer> getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Map<String, Integer> fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getIntervaloaplicacion() {
        return intervaloaplicacion + "hr";
    }

    public void setIntervaloaplicacion(String intervaloaplicacion) {
        this.intervaloaplicacion = intervaloaplicacion;
    }

}
