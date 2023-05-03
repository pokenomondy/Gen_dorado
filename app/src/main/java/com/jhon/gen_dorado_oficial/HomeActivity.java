package com.jhon.gen_dorado_oficial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jhon.gen_dorado_oficial.Adaptador.Familiares_adaptador;
import com.jhon.gen_dorado_oficial.Objetos.Familiares;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;


public class HomeActivity extends AppCompatActivity{

    Toolbar toolbarhome;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;

    Button buscar_paciente;
    EditText familiarcedula,apodofamiliar;
    LinearLayoutCompat vista_paciente,vista_acudiente;
    RecyclerView recycler_familiares;
    List<Familiares> familiaresList;
    Familiares_adaptador familiares_adaptador;
    LinearLayoutCompat noboton_medicamentos,nobotonminijuegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        toolbarhome = findViewById(R.id.toolbar_home);
        buscar_paciente = findViewById(R.id.buscar_paciente);
        setSupportActionBar(toolbarhome);
        familiarcedula = findViewById(R.id.familiarcedula);
        apodofamiliar = findViewById(R.id.apodofamiliar);
        //VISTAS
        vista_acudiente = findViewById(R.id.vista_Acudiente);
        vista_paciente = findViewById(R.id.vistapaciente);
        recycler_familiares = findViewById(R.id.recycler_familiares);
        familiaresList = new ArrayList<>();
        //botones de pacientes
        noboton_medicamentos = findViewById(R.id.noboton_medicamentos);
        nobotonminijuegos = findViewById(R.id.nobotonminijuegos);

        /*/inicializamos base de datos/*/

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
        updatenombretoolbar();


    }

    //menu inflado en el toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return true;
    }

    //opciones del menu creado
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.userdashboard){
            Intent actualizardatos = new Intent(HomeActivity.this,DashboardUser.class);
            startActivity(actualizardatos);
        }else {

        }

        return true;
    }

    //Aqui separamos si es paciente o acudiente
    private void updatenombretoolbar(){
        Query query = BASE_DE_DATOS.orderByChild("Numero de celular").equalTo(firebaseUser.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //iterar para buscar por numero de celular
                for (DataSnapshot ds : snapshot.getChildren()){
                    //OBTENER VALORES DE BASE DA DATOS
                    String nombre = ""+ds.child("Nombre").getValue();
                    String rol = ""+ds.child("rol").getValue();
                    String uid = ""+ds.child("uid").getValue();


                    //INCRUSTAR DATOS EN VISTAS OBLIGADO
                    getSupportActionBar().setTitle("Hola "+nombre);
                        /*/Este if va a separar acudientes de usuaraios /*/
                        if (rol.equals("Paciente")){
                            Toast.makeText(HomeActivity.this,"Paciente",Toast.LENGTH_SHORT).show();
                            vista_acudiente.setVisibility(View.GONE);
                            vista_paciente.setVisibility(View.VISIBLE);
                            esPaciente();
                        }else {
                            Toast.makeText(HomeActivity.this, "Acudiente", Toast.LENGTH_SHORT).show();
                            vista_acudiente.setVisibility(View.VISIBLE);
                            vista_paciente.setVisibility(View.GONE);
                            esAcudiente();
                        }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });



    }
    //cuando tenemos un paciente
    private void esPaciente() {
        noboton_medicamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,Medicamentos.class);
                startActivity(intent);
            }
        });

        nobotonminijuegos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this,"sirve boton",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //De aqui pa abajo, cuando sean acudientess
    private void esAcudiente() {
        //botón para registrar nuevo paciente
        buscar_paciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cargardatosesAcudiente();
                Toast.makeText(HomeActivity.this,"Funciona el boton",Toast.LENGTH_SHORT).show();
                //Agregar paciente nuevo, vamos a ver

            }
        });

        //Recycler de paciente
        recycler_familiares.setLayoutManager(new LinearLayoutManager(this));
        familiares_adaptador = new Familiares_adaptador(familiaresList);
        recycler_familiares.setAdapter(familiares_adaptador);
        //Reccycle view base de datos que funciona
        BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("familiar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                familiaresList.removeAll(familiaresList);
                for (DataSnapshot snapshot:
                dataSnapshot.getChildren()){
                    Familiares familiar = snapshot.getValue(Familiares.class);
                    familiaresList.add(familiar);
                }
                familiares_adaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void cargardatosesAcudiente() {
        Query query = BASE_DE_DATOS.orderByChild("num_cedula").equalTo(familiarcedula.getText().toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    //OBTENER VALORES DE BASE DA DATOS
                    String nombre = ""+ds.child("Nombre").getValue();
                    String cedula = ""+ds.child("num_cedula").getValue();
                    String  uid = ""+ds.child("uid").getValue();


                    //AGREGR NUEVO PACIENTE
                    Familiares familiar = new Familiares(cedula,nombre,apodofamiliar.getText().toString(),uid);
                    BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("familiar").push().setValue(familiar);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this,"Esta mal la cedula",Toast.LENGTH_SHORT).show();

            }
        });
    }

    //Metodos cuando sean PACIENTES
}