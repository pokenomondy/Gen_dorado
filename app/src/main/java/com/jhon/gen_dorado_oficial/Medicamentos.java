package com.jhon.gen_dorado_oficial;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.database.DataSetObserver;
import android.hardware.lights.LightState;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhon.gen_dorado_oficial.Adaptador.Medicamentos_adaptador;
import com.jhon.gen_dorado_oficial.Objetos.Familiares;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Medicamentos extends AppCompatActivity {
    RecyclerView recycler_medicamentos;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;
    //Lista
    List<com.jhon.gen_dorado_oficial.Objetos.Medicamentos> medicamentoList ;
    //adaptador
    Medicamentos_adaptador medicamentos_adapter;
    FloatingActionButton floatbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);


        //Recycler
        recycler_medicamentos = findViewById(R.id.recycler_medicamentos);
        medicamentoList = new ArrayList<>();
        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
        //adaptador
        recycler_medicamentos.setLayoutManager(new LinearLayoutManager(this));
        medicamentos_adapter =  new Medicamentos_adaptador(medicamentoList);
        recycler_medicamentos.setAdapter(medicamentos_adapter);
        //floatbutto
        floatbutton = findViewById(R.id.floatbuttonmedicamento);


        //AHORA LLAMAMOS PARA ACTUALIZAR Y EMPEZAR A METER DATOS
        BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                medicamentoList.removeAll(medicamentoList);
                for (DataSnapshot snapshot:
                        dataSnapshot.getChildren()){
                    com.jhon.gen_dorado_oficial.Objetos.Medicamentos medicamento = snapshot.getValue(com.jhon.gen_dorado_oficial.Objetos.Medicamentos.class);
                    medicamentoList.add(medicamento);
                }
                medicamentos_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        registromedicamento();

    }

    private void registromedicamento() {
        floatbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(Medicamentos.this);
                dialog.setContentView(R.layout.item_add_medicamento);
                Button btnsendmedicamento;
                EditText nombremedicamento;
                EditText dosismedicamento;

                btnsendmedicamento = dialog.findViewById(R.id.btnsendmedicamento);
                nombremedicamento = dialog.findViewById(R.id.nombremedicamento);
                dosismedicamento = dialog.findViewById(R.id.dosismedicamento);
                //hora
                LocalDateTime ahora = LocalDateTime.now();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String fechaHoraActual = ahora.format(formato);



                //oprimir boton dentro del dialog
                btnsendmedicamento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.jhon.gen_dorado_oficial.Objetos.Medicamentos medicamentos = new com.jhon.gen_dorado_oficial.Objetos.Medicamentos("Acetaminofen","8:30pm","12:00","siguiente en","2","2");
                        BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").push().setValue(medicamentos);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}