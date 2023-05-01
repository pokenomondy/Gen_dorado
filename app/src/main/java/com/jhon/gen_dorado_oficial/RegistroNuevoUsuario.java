package com.jhon.gen_dorado_oficial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistroNuevoUsuario extends AppCompatActivity {

    Button btnbtnactinfo;
    EditText editnombre,editapellido,editcedula,editrol;
    FirebaseAuth firebaseAuth;
    Spinner spinnerrol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_nuevo_usuario);

        btnbtnactinfo = findViewById(R.id.btnactinfo);
        editnombre = findViewById(R.id.editnombre);
        editapellido = findViewById(R.id.editapellido);
        editcedula = findViewById(R.id.editcedula);
        editrol = findViewById(R.id.editrol);

        //inicio de base de datos
        firebaseAuth = FirebaseAuth.getInstance();

        //Spinner para el rol
        spinnerrol = findViewById(R.id.spinerrol);
        String [] rolspinnerrespuestas = {"Paciente","Acudiente"};
        ArrayAdapter <String> adadpterrol = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,rolspinnerrespuestas);
        spinnerrol.setAdapter(adadpterrol);

        btnbtnactinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                //obtener UID
                assert user != null;
                String uid = user.getUid();
                String name = editnombre.getText().toString();
                String num_cedula = editcedula.getText().toString();
                String rol = editrol.getText().toString();
                String apellido = editapellido.getText().toString();

                HashMap<Object,String> DatosUsuario = new HashMap<>();

                DatosUsuario.put("uid",uid);
                DatosUsuario.put("Nombre",name);
                DatosUsuario.put("Apellido",apellido);
                DatosUsuario.put("num_cedula",num_cedula);
                DatosUsuario.put("rol",rol);
                // Espacio para imagen
                DatosUsuario.put("imagen","");
                DatosUsuario.put("Numero de celular",user.getPhoneNumber());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("USUARIOS");
                reference.child(uid).setValue(DatosUsuario);
                Toast.makeText(RegistroNuevoUsuario.this,"Registro exitoso",Toast.LENGTH_SHORT).show();

                Intent ahome = new Intent(RegistroNuevoUsuario.this,HomeActivity.class);
                startActivity(ahome);


            }
        });

    }

    public void mostrarselecccionador(View view){
        String seleccionado = spinnerrol.getSelectedItem().toString();
        if (seleccionado.equals("Paciente")){
            Toast.makeText(RegistroNuevoUsuario.this,"Paciente",Toast.LENGTH_SHORT);

        } else if (seleccionado.equals("Acudiente")) {
            Toast.makeText(RegistroNuevoUsuario.this,"Acudiente",Toast.LENGTH_SHORT);

        }
    }
}