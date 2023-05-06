package com.jhon.gen_dorado_oficial.Objetos;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

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

        Calendar fecha_inicio = Calendar.getInstance();
        fecha_inicio.set( año_init, mes_init - 1, dia_init, hora_init, min_init, sec_init);
        return fecha_inicio.getTime();
    }


    public String getCalcsigmedicamento(){

        Date inicio = obtenerFechaInicio();
        int intervalo_medicamento = Integer.parseInt(this.intervaloaplicacion);

        Calendar fecha_inicio = Calendar.getInstance();
        fecha_inicio.setTime(inicio);

        fecha_inicio.add(Calendar.HOUR, intervalo_medicamento);

        Date fecha_inicio_add = fecha_inicio.getTime();
        Date fecha_ahora = new Date();

       //MILISEGUNDOS
        long diferencia = fecha_inicio_add.getTime() - fecha_ahora.getTime();
        long moduloHoras = 24;
        int suma = 0;

        if(intervalo_medicamento >= 24){
            moduloHoras = intervalo_medicamento;
        }

        //HORAS
        int diferencia_minutos = (int)(diferencia / (1000 * 60)) % 60;
        int diferencia_horas = (int) (diferencia/(1000 * 60 * 60));

        if (diferencia_horas < 0 || diferencia_minutos < 0){
            return "Retrasado en " + (diferencia_horas * -1) + " horas y " + (diferencia_minutos * -1) + " minutos";
        }else {
            return "Siguiente en " + diferencia_horas + " horas y " + diferencia_minutos + " minutos";
        }
    }

    public Map<String, Integer> getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Map<String, Integer> fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getIntervaloaplicacion() {
        return intervaloaplicacion;
    }

    public void setIntervaloaplicacion(String intervaloaplicacion) {
        this.intervaloaplicacion = intervaloaplicacion;
    }

}
