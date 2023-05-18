package com.jhon.gen_dorado_oficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DashboardUser extends AppCompatActivity {

    /*/inicializamos base de datos/*/
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;

    //Inicialozamos variables de show
    TextView shownombre,showapellido,showrol,showcedula;
    ImageView imgperfil;
    Button cerrarsesion,btneditarinfo,btn_regresar_dash;
    Toolbar toolbar_dashboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);

        /*/inicializamos base de datos/*/

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
        updatenombretoolbar();

        //Iniciamos variables de vista
        shownombre = findViewById(R.id.shownombre);
        showapellido = findViewById(R.id.showapellido);
        showrol = findViewById(R.id.showrol);
        imgperfil = findViewById(R.id.imgperfilshow);
        showcedula = findViewById(R.id.showcedula);
        cerrarsesion = findViewById(R.id.Cerrarsesion);
        btneditarinfo= findViewById(R.id.btneditarinfo);
        //modificar color y nombre del toolbar
        toolbar_dashboard = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar_dashboard);
        getSupportActionBar().setTitle("Dashboard");

        //regresar dash
        btn_regresar_dash = findViewById(R.id.btn_regresar_dash);
        btn_regresar_dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardUser.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesionnn();
            }
        });
        btneditarinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardUser.this,EditarUsuarioRecurrente.class);
                startActivity(intent);
            }
        });
    }

    private void updatenombretoolbar(){
        Query query = BASE_DE_DATOS.orderByChild("Numero de celular").equalTo(firebaseUser.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //iterar para buscar por numero de celular
                for (DataSnapshot ds : snapshot.getChildren()){
                    //OBTENER VALORES DE BASE DA DATOS
                    String nombre = ""+ds.child("Nombre").getValue();
                    String apellido = ""+ds.child("Apellido").getValue();
                    String fotoperfil = ""+ds.child("imagen").getValue();
                    String cedula = ""+ds.child("num_cedula").getValue();
                    String rol = ""+ds.child("rol").getValue();
                    //INCRUSTAR DATOS EN VISTAS OBLIGADO
                    shownombre.setText(nombre);
                    showapellido.setText(apellido);
                    showcedula.setText(cedula);
                    showrol.setText(rol);

                    //IMAGEN
                    try{
                        //Si existe iagen en la base de datos
                        Picasso.get().load(fotoperfil).placeholder(R.drawable.userprefault).into(imgperfil);
                    }catch (Exception e){
                        //Si no existe imagen en base de datos, mostrar comun
                        Picasso.get().load(R.drawable.userprefault).into(imgperfil);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });



    }

    private void CerrarSesionnn(){
        firebaseAuth.signOut();
        startActivity(new Intent(DashboardUser.this,MainActivity.class));

    }
}