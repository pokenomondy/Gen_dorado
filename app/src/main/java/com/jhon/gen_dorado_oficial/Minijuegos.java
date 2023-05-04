package com.jhon.gen_dorado_oficial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Minijuegos extends AppCompatActivity {

    Button btnRecordar_Patrones, btnMinJuego2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minijuegos);
        btnRecordar_Patrones = findViewById(R.id.RecordarPatrones);
        btnMinJuego2 = findViewById(R.id.Minijuego2);
        init();
    }

    public void init(){

        btnRecordar_Patrones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Minijuegos.this,recordarPatrones.class);
                startActivity(intent);
            }
        });

        btnMinJuego2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Minijuegos.this,"Boton sirviedo", Toast.LENGTH_SHORT).show();
            }
        });

    }




}