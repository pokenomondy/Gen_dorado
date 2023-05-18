package com.jhon.gen_dorado_oficial.Objetos;

import android.content.Context;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.WorkManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Medicamentos {
    String medicamento; //nombre medicamento
    int dosis, hora, minuto; //cuantas debe tomar
    Map<String, Integer> fechainicio; //cuando empezo

    private  String id; //Variable de id del nodo, para poder entrar
    int num_tomado ; //numero pastilla tomado
        String uiseruid;


    public Medicamentos() {
    }

    public Medicamentos(String medicamento, int dosis, Map<String, Integer> fechainicio, String id, int num_tomado, String uiseruid, int hora, int minuto) {
        this.medicamento = medicamento;
        this.dosis = dosis;
        this.fechainicio = fechainicio;
        this.hora = hora;
        this.minuto = minuto;
        this.id = id;
        this.num_tomado = num_tomado;
        this.uiseruid = uiseruid;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
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
        calendar.add(Calendar.HOUR, hora);
        calendar.add(Calendar.MINUTE, minuto);
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


    public String hora_tomada_String(){
        Date fecha = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        String fechaString = formatter.format(fecha);

        return fechaString;
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

    public long obtenerMillis(){
        Date inicio = obtenerFechaInicio();

        Calendar fecha_inicio = Calendar.getInstance();
        fecha_inicio.setTime(inicio);

        fecha_inicio.add(Calendar.HOUR, hora);
        fecha_inicio.add(Calendar.MINUTE, minuto);

        Date fecha_inicio_add = fecha_inicio.getTime();
        Date fecha_ahora = new Date();

        //MILISEGUNDOS
        long diferencia = fecha_inicio_add.getTime() - fecha_ahora.getTime();

        return diferencia;
    }

    public String getCalcsigmedicamento(){

        Date inicio = obtenerFechaInicio();

        Calendar fecha_inicio = Calendar.getInstance();
        fecha_inicio.setTime(inicio);

        fecha_inicio.add(Calendar.HOUR, hora);
        fecha_inicio.add(Calendar.MINUTE, minuto);

        Date fecha_inicio_add = fecha_inicio.getTime();
        Date fecha_ahora = new Date();

       //MILISEGUNDOS
        long diferencia = fecha_inicio_add.getTime() - fecha_ahora.getTime();


        //HORAS
        int diferencia_minutos = (int)(diferencia / (1000 * 60)) % 60;
        int diferencia_horas = (int) (diferencia/(1000 * 60 * 60));

        if (diferencia_horas < 0 || diferencia_minutos < 0){
            return "Retrasado en " + (diferencia_horas * -1) + " horas y " + (diferencia_minutos * -1) + " minutos";
        }else {
            return "Siguiente en " + diferencia_horas + " horas y " + diferencia_minutos + " minutos";
        }
    }

    public String getCalcTomado(){

        Date inicio = obtenerFechaInicio();

        Calendar fecha_inicio = Calendar.getInstance();
        fecha_inicio.setTime(inicio);

        fecha_inicio.add(Calendar.HOUR, hora);
        fecha_inicio.add(Calendar.MINUTE, minuto);

        Date fecha_inicio_add = fecha_inicio.getTime();
        Date fecha_ahora = new Date();

        //MILISEGUNDOS
        long diferencia = fecha_inicio_add.getTime() - fecha_ahora.getTime();

        //HORAS
        int diferencia_minutos = (int)(diferencia / (1000 * 60)) % 60;
        int diferencia_horas = (int) (diferencia/(1000 * 60 * 60));

        if (diferencia_horas < 0 || diferencia_minutos < 0){
            return "Tomado " + (diferencia_horas * -1) + " horas y " + (diferencia_minutos * -1) + " minutos despues de la hora";
        }else {
            return "Tomado " + diferencia_horas + " horas y " + diferencia_minutos + " minutos antes de la hora";
        }
    }

    public Map<String, Integer> getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Map<String, Integer> fechainicio) {
        this.fechainicio = fechainicio;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum_tomado() {
        return num_tomado;
    }

    public void setNum_tomado(int num_tomado) {
        this.num_tomado = num_tomado;
    }

    public String getUiseruid() {
        return uiseruid;
    }

    public void setUiseruid(String uiseruid) {
        this.uiseruid = uiseruid;
    }
}
