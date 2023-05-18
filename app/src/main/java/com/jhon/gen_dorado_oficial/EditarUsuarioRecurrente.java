package com.jhon.gen_dorado_oficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditarUsuarioRecurrente extends AppCompatActivity {

    EditText editnombre_Recurrente,editapellido_recurrente;
    TextView showrol,showcedula;
    String editnombre,editapellido;
    Button btrneditarinfo,btn_regresar_dash_editar;
    //bases de datos
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;
    Toolbar toolvar_editarusuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario_recurrente);
        //Inicializar variables
        editnombre_Recurrente = findViewById(R.id.editnombre_Recurrente);
        editapellido_recurrente = findViewById(R.id.editapellido_recurrente);
        showcedula = findViewById(R.id.showcedula);
        showrol = findViewById(R.id.showrol);
        btrneditarinfo = findViewById(R.id.btrneditarinfo);
        editnombre = editnombre_Recurrente.getText().toString();
        editapellido = editapellido_recurrente.getText().toString();

        /*/inicializamos base de datos/*/
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
        updatedatos();

        //toolbar editar usuario
        toolvar_editarusuario = findViewById(R.id.toolvar_editarusuario);
        setSupportActionBar(toolvar_editarusuario);
        getSupportActionBar().setTitle("Editar información");

        //regresar a dashboard principal
        btn_regresar_dash_editar = findViewById(R.id.btn_regresar_dash_editar);
        btn_regresar_dash_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarUsuarioRecurrente.this,DashboardUser.class);
                startActivity(intent);
            }
        });


        btrneditarinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreusar = "";
                String apellidousar = "";
                if (editnombre_Recurrente.getText().toString().equals("")){
                    nombreusar = editnombre;
                }else {
                    nombreusar = editnombre_Recurrente.getText().toString();
                }
                if(editapellido_recurrente.getText().toString().equals("")){
                    apellidousar = editapellido;
                }else{
                    apellidousar = editapellido_recurrente.getText().toString();
                }

                BASE_DE_DATOS.child(firebaseAuth.getUid()).child("Nombre").setValue(nombreusar);
                BASE_DE_DATOS.child(firebaseAuth.getUid()).child("Apellido").setValue(apellidousar);
                Toast.makeText(EditarUsuarioRecurrente.this,"Cambio realizado con exito",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updatedatos() {
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
                    String cedula = ""+ds.child("num_cedula").getValue();
                    String apelldio = ""+ds.child("Apellido").getValue();

                    showrol.setText(rol);
                    showcedula.setText(cedula);
                    if (editnombre.equals("")){
                        editnombre = nombre;
                    }
                    if (editapellido.equals("")){
                        editapellido = apelldio;
                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
}