package com.jhon.gen_dorado_oficial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Minijuegos extends AppCompatActivity {

    Button btnRecordar_Patrones;
    Toolbar toolbar_minijuegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minijuegos);
        btnRecordar_Patrones = findViewById(R.id.RecordarPatrones);
        init();
        //modificar color y nombre del toolbar
        toolbar_minijuegos = findViewById(R.id.toolbar_minijuegos);
        setSupportActionBar(toolbar_minijuegos);
        getSupportActionBar().setTitle("Minijuegos");



    }

    public void init(){

        btnRecordar_Patrones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Minijuegos.this,recordarPatrones.class);
                startActivity(intent);
            }
        });

    }




}