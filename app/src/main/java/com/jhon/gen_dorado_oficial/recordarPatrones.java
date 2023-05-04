package com.jhon.gen_dorado_oficial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class recordarPatrones extends AppCompatActivity {

    // Variables definidas
    ImageButton imb00, imb01, imb02, imb03, imb04, imb05, imb06, imb07, imb08, imb09, imb10, imb11, imb12, imb13, imb14, imb15;
    ImageButton[] tablero = new ImageButton[16];

    Button btn_reiniciar, btn_salir;

    int puntuacion;
    int aciertos;

    //Imagenes
    int[] imagenes;
    int fondo;

    //variables de juego
    ArrayList<Integer> arrayDesordenada;
    ImageButton primero;
    int numeroPrimero, numeroSegundo;
    boolean bloqueo = false;
    final Handler handler = new Handler() {
        @Override
        public void publish(LogRecord logRecord) {
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordar_patrones);
        init();
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

        btn_reiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
            }
        });

        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void cargarPuntuacion(){
        puntuacion = 0;
        aciertos = 0;
    }

    private void cargarImagenes(){
        imagenes = new int[]{
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

    private void init(){
        cargarTablero();
        cargarBotones();
        cargarPuntuacion();
        cargarImagenes();
        arrayDesordenada = barajar(imagenes.length);
        for(int i=0; i<12; i++){
            tablero[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            tablero[i].setImageResource(imagenes[arrayDesordenada.get(i)]);
        }
    }

}