package com.jhon.gen_dorado_oficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class RegistroNuevoUsuario extends AppCompatActivity {

    Button btnbtnactinfo;
    EditText editnombre,editapellido,editcedula;
    FirebaseAuth firebaseAuth;
    Spinner spinnerrol;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;
    String cedulaverificar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_nuevo_usuario);

        btnbtnactinfo = findViewById(R.id.btnactinfo);
        editnombre = findViewById(R.id.editnombre);
        editapellido = findViewById(R.id.editapellido);
        editcedula = findViewById(R.id.editcedula);

        //inicio de base de datos
        firebaseAuth = FirebaseAuth.getInstance();
        cedulaverificar = "nombreimposiblequepongaesto";
        //Spinner para el rol
        spinnerrol = findViewById(R.id.spinerrol);
        String [] rolspinnerrespuestas = {"Paciente","Acudiente"};
        ArrayAdapter <String> adadpterrol = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,rolspinnerrespuestas);
        spinnerrol.setAdapter(adadpterrol);

        /*/inicializamos base de datos/*/
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");

        //verificar si cedula ya existe
        editcedula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cedula = s.toString().trim();
                if (!cedula.isEmpty()) {
                    Query query = BASE_DE_DATOS.orderByChild("num_cedula").equalTo(cedula);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // La cédula ya existe en la base de datos, mostrar mensaje de error
                                        Toast.makeText(RegistroNuevoUsuario.this,"Cedula ya registrada, cambiala para poder registrarte",Toast.LENGTH_SHORT).show();
                                        btnbtnactinfo.setEnabled(false);
                                    }
                                });

                            } else {
                                // La cédula no existe en la base de datos, permitir registro
                                btnbtnactinfo.setEnabled(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        btnbtnactinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String seleccionado = spinnerrol.getSelectedItem().toString();

                //REGISTRO

                        //Comprobamos que lo que ingresamos no sea igual
                            if (editnombre.getText().toString().isEmpty() ||
                                    editapellido.getText().toString().isEmpty() ||
                                    editcedula.getText().toString().isEmpty() ||
                                    seleccionado.isEmpty()){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegistroNuevoUsuario.this,"Se deben llenar todas las casillas",Toast.LENGTH_SHORT);
                                    }
                                });
                            }else{
                                if (cedulaverificar.equals(editcedula.getText().toString())) {
                                    Toast.makeText(RegistroNuevoUsuario.this,"Cedula ya registrada",Toast.LENGTH_SHORT);
                                }else {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    //obtener UID
                                    assert user != null;
                                    String uid = user.getUid();
                                    String name = editnombre.getText().toString();
                                    String num_cedula = editcedula.getText().toString();
                                    String apellido = editapellido.getText().toString();

                                    HashMap<Object,String> DatosUsuario = new HashMap<>();

                                    DatosUsuario.put("uid",uid);
                                    DatosUsuario.put("Nombre",name);
                                    DatosUsuario.put("Apellido",apellido);
                                    DatosUsuario.put("num_cedula",num_cedula);
                                    DatosUsuario.put("rol",seleccionado);
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


                            }

                        }

        });

    }

}