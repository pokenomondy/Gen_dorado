package com.jhon.gen_dorado_oficial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity{

    Toolbar toolbarhome;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbarhome = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbarhome);

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

    private void updatenombretoolbar(){
        Query query = BASE_DE_DATOS.orderByChild("Numero de celular").equalTo(firebaseUser.getPhoneNumber());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //iterar para buscar por numero de celular
                for (DataSnapshot ds : snapshot.getChildren()){
                    //OBTENER VALORES DE BASE DA DATOS
                    String nombre = ""+ds.child("Nombre").getValue();
                    //INCRUSTAR DATOS EN VISTAS OBLIGADO
                    getSupportActionBar().setTitle("Hola "+nombre);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });



    }

}