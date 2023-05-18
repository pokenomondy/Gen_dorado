package com.jhon.gen_dorado_oficial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

public class recordarPatrones extends AppCompatActivity {


    //Cronometro
    TextView temporizador;
    CountDownTimer countDownTimer;
    int gameTime;
    Dialog dialog;

    // Variables definidas
    ImageButton imb00, imb01, imb02, imb03, imb04, imb05, imb06, imb07, imb08, imb09, imb10, imb11, imb12, imb13, imb14, imb15;
    ImageButton[] tablero = new ImageButton[16];

    ImageView lgro00, lgro01, lgro02, lgro03, lgro04;

    Button btn_reiniciar, btn_salir, logros, salirDialog;
    TextView puntajeText, puntajeMaximo;

    int puntuacion;
    int aciertos;
    int maximoScore;

    //Imagenes
    int[] imagenes;
    int fondo;

    //variables de juego
    ArrayList<Integer> arrayDesordenada;
    ImageButton primero;
    int numeroPrimero, numeroSegundo;
    boolean bloqueo = false, firtsTime;
    final android.os.Handler handler = new Handler();
    int normalTime = 60;
    int addTime = 30;
    int nivel = 0;
    boolean[] logrosVerificar = new boolean[5];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordar_patrones);
        cargar();
        iniciar_Temporizador(normalTime);
    }

    private void cargarTablero(){
        imb00 = findViewById(R.id.btn00);
        imb01 = findViewById(R.id.btn01);
        imb02 = findViewById(R.id.btn02);
        imb03 = findViewById(R.id.btn03);
        imb04 = findViewById(R.id.btn04);
        imb05 = findViewById(R.id.btn05);
        imb06 = findViewById(R.id.btn06);
        imb07 = findViewById(R.id.btn07);
        imb08 = findViewById(R.id.btn08);
        imb09 = findViewById(R.id.btn09);
        imb10 = findViewById(R.id.btn10);
        imb11 = findViewById(R.id.btn11);
        imb12 = findViewById(R.id.btn12);
        imb13 = findViewById(R.id.btn13);
        imb14 = findViewById(R.id.btn14);
        imb15 = findViewById(R.id.btn15);

        tablero[0] = imb00;
        tablero[1] = imb01;
        tablero[2] = imb02;
        tablero[3] = imb03;
        tablero[4] = imb04;
        tablero[5] = imb05;
        tablero[6] = imb06;
        tablero[7] = imb07;
        tablero[8] = imb08;
        tablero[9] = imb09;
        tablero[10] = imb10;
        tablero[11] = imb11;
        tablero[12] = imb12;
        tablero[13] = imb13;
        tablero[14] = imb14;
        tablero[15] = imb15;

    }

    private void cargarBotones(){
        btn_reiniciar = findViewById(R.id.btnreiniciar);
        btn_salir = findViewById(R.id.btnsalir);
        logros = findViewById(R.id.logrosButtom);
        salirDialog = dialog.findViewById(R.id.salir_logros);

        btn_reiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                puntuacion = 0;
                iniciar_Temporizador(normalTime);
            }
        });

        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        logros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        salirDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }


    private void iniciar_Temporizador(int Time){
        detenerTemporizador();
        gameTime = Time;
        countDownTimer = new CountDownTimer(Time * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (firtsTime){
                    nivel=0;
                    firtsTime = false;
                    init();
                }

                gameTime--;

                temporizador.setText(String.valueOf(gameTime));


            }

            public void onFinish() {
                firtsTime = true;

                temporizador.setText("Tiempo terminado!");

                if (puntuacion > maximoScore){
                    Toast.makeText(recordarPatrones.this, "Enhorabuena, haz alcanzado un nuevo record!", Toast.LENGTH_SHORT).show();
                    
                    
                    maximoScore = puntuacion;
                    //Guardar maximoScore en Firebase
                    
                    
                    //Guardar
                    
                    puntajeMaximo.setText("Puntaje maximo: " + maximoScore);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    //Espera de 2 segundos
                    }
                }, 2000);

                Toast.makeText(recordarPatrones.this, "Iniciando nuevo juego!", Toast.LENGTH_SHORT).show();


                puntuacion = 0;
                init();

            }
        }.start();
    }

    public void detenerTemporizador(){
        if (countDownTimer!=null){
            countDownTimer.cancel();
            firtsTime = true;
        }
    }

    private void cargarPuntuacion(){
        //Obtener maximo Score desde Firebase
        maximoScore = 0; //Incluir el maximo score en la variable
        aciertos = 0;
    }

    private void cargarLogros(){
        logrosVerificar[0] = true;
        logrosVerificar[1] = true;
        logrosVerificar[2] = true;
        logrosVerificar[3] = true;
        logrosVerificar[4] = true;
    }

    private void cargarImagenes(){
        lgro00 = dialog.findViewById(R.id.logro00);
        lgro01 = dialog.findViewById(R.id.logro01);
        lgro02 = dialog.findViewById(R.id.logro02);
        lgro03 = dialog.findViewById(R.id.logro03);
        lgro04 = dialog.findViewById(R.id.logro04);

        cargarLogros();

        if(logrosVerificar[0]){
            lgro00.setImageResource(R.drawable.logro00);
        }
        if(logrosVerificar[1]){
            lgro01.setImageResource(R.drawable.logro01);
        }
        if(logrosVerificar[2]){
            lgro02.setImageResource(R.drawable.logro02);
        }
        if(logrosVerificar[3]){
            lgro03.setImageResource(R.drawable.logro03);
        }
        if(logrosVerificar[4]){
            lgro04.setImageResource(R.drawable.logro04);
        }

        imagenes = new int[]{
                R.drawable.imgre00,
                R.drawable.imgre01,
                R.drawable.imgre02,
                R.drawable.imgre03,
                R.drawable.imgre04,
                R.drawable.imgre05,
                R.drawable.imgre06,
                R.drawable.imgre07,
        };

        fondo = R.drawable.fondo;
    }

    private ArrayList<Integer> barajar(int longitud){
        ArrayList<Integer> resultado = new ArrayList<Integer>();
        for(int i=0; i<longitud*2; i++){
            resultado.add(i % longitud);
        }
        Collections.shuffle(resultado);

        return resultado;
    }


    private void cargarTexViews(){
        puntajeText = findViewById(R.id.puntuacionJuego);
        puntajeMaximo = findViewById(R.id.puntuacionmaxima);
        temporizador = findViewById(R.id.Temporizador);
        puntajeMaximo.setText("Puntuacion maxima: " + maximoScore);
    }


    private void comprobar(int i, final ImageButton imgb){
        if (primero == null){
            primero = imgb;
            primero.setScaleType(ImageView.ScaleType.CENTER_CROP);
            primero.setImageResource(imagenes[arrayDesordenada.get(i)]);
            primero.setEnabled(false);
            numeroPrimero = arrayDesordenada.get(i);
        }else {
            bloqueo = true;
            imgb.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgb.setImageResource(imagenes[arrayDesordenada.get(i)]);
            imgb.setEnabled(false);
            numeroSegundo = arrayDesordenada.get(i);
            if (numeroPrimero == numeroSegundo){
                primero = null;
                bloqueo = false;
                aciertos++;
                puntuacion++;
                puntajeText.setText("Puntuacion: " + puntuacion);
                if(aciertos == imagenes.length){

                    aciertos = 0;

                    Toast.makeText(this, "+"+String.valueOf(addTime)+"segs", Toast.LENGTH_SHORT).show();
                    iniciar_Temporizador(gameTime+addTime);

                    if (nivel<4){
                        addTime-=5;
                    }

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(recordarPatrones.this, "Iniciando nueva ronda!", Toast.LENGTH_SHORT).show();
                        }
                    },1500);
                }
            }else{
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        primero.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        primero.setImageResource(fondo);
                        primero.setEnabled(true);

                        imgb.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imgb.setImageResource(fondo);
                        imgb.setEnabled(true);

                        bloqueo = false;
                        primero = null;
                    }
                },1000);

            }
        }
    }

    private void cargar(){
        cargarTablero();
        cargarDialog();
        cargarBotones();
        cargarTexViews();
        cargarImagenes();
        cargarPuntuacion();
        firtsTime = true;
    }

    private void cargarDialog(){
        dialog = new Dialog(recordarPatrones.this);
        dialog.setContentView(R.layout.dialog_logros_paciente);
    }

    private void init(){
        puntajeText.setText("Puntuacion: "+puntuacion);

        arrayDesordenada = barajar(imagenes.length);

        for(int i=0; i<tablero.length ; i++){
            tablero[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            tablero[i].setImageResource(imagenes[arrayDesordenada.get(i)]);

        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<tablero.length ; i++){
                    tablero[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
                    tablero[i].setImageResource(fondo);
                }
            }
        },1000);

        for(int i=0; i<tablero.length; i++){
            final int j = i;
            tablero[i].setEnabled(true);
            tablero[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!bloqueo){
                        comprobar(j, tablero[j]);
                    }
                }
            });
        }

    }

}