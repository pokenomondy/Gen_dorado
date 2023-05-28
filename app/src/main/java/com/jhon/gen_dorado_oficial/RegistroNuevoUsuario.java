package com.jhon.gen_dorado_oficial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistroNuevoUsuario extends AppCompatActivity {

    Button btnbtnactinfo;
    ImageView editbtnfoto;
    CircleImageView imgmostrarprimero;
    EditText editnombre,editapellido,editcedula;
    FirebaseAuth firebaseAuth;
    Spinner spinnerrol;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;



    String cedulaverificar;

    //Variables para foto
    StorageReference storageReference;
    String storage_path = "profile_pic/*";
    private static  final int COD_SEL_STORAGE = 200;
    private  static  final int CODE_SEL_IMAGE = 300;
    private Uri image_url;
    String photo = "photo";
    String idd;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_nuevo_usuario);

        btnbtnactinfo = findViewById(R.id.btnactinfo);
        editnombre = findViewById(R.id.editnombre);
        editapellido = findViewById(R.id.editapellido);
        editcedula = findViewById(R.id.editcedula);
        imgmostrarprimero = findViewById(R.id.imgmostrarprimero);

        cedulaverificar = "nombreimposiblequepongaesto";
        //Spinner para el rol
        spinnerrol = findViewById(R.id.spinerrol);
        String [] rolspinnerrespuestas = {"Paciente","Acudiente"};
        ArrayAdapter <String> adadpterrol = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,rolspinnerrespuestas);
        spinnerrol.setAdapter(adadpterrol);


        /*/inicializamos base de datos/*/
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");

        //imagen editar

        storageReference = FirebaseStorage.getInstance().getReference();
        editbtnfoto = findViewById(R.id.editbtnfoto);
        progressDialog = new ProgressDialog(this);

        editbtnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });


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
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
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

                        //Comprobamos que todos los datos esten ingresados
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

                                    HashMap<String,Object> DatosUsuario = new HashMap<>();

                                    DatosUsuario.put("uid",uid);
                                    DatosUsuario.put("Nombre",name);
                                    DatosUsuario.put("Apellido",apellido);
                                    DatosUsuario.put("num_cedula",num_cedula);
                                    DatosUsuario.put("rol",seleccionado);


                                    // Espacio para imagen
                                    DatosUsuario.put("Numero de celular",user.getPhoneNumber());

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("USUARIOS");
                                    reference.child(uid).updateChildren(DatosUsuario);
                                    Toast.makeText(RegistroNuevoUsuario.this,"Registro exitoso",Toast.LENGTH_SHORT).show();

                                    Intent ahome = new Intent(RegistroNuevoUsuario.this,HomeActivity.class);
                                    startActivity(ahome);
                                    finish();
                                }


                            }

                        }

        });

        //cargar datos para ver la foto
        updatedatos();

    }


    private void uploadPhoto() {
        //cargamos para buscar la foto
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i,CODE_SEL_IMAGE);
    }

    //metodo para obtener resultados de la activiy, datos de la foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == CODE_SEL_IMAGE){
                image_url = data.getData();
                subirPhoto(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //metodo para subir la foto
    private void subirPhoto(Uri image_url) {
        Toast.makeText(RegistroNuevoUsuario.this,"Actualizando foto",Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();
        String rute_storage_photo = storage_path + "" + photo + "" + firebaseUser.getUid() + "" + idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                    if (uriTask.isSuccessful()){
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String dowload_uri = uri.toString();
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("imagen",dowload_uri);
                                map.put("Numero de celular",firebaseUser.getPhoneNumber());
                                BASE_DE_DATOS.child(firebaseUser.getUid()).updateChildren(map);
                                Toast.makeText(RegistroNuevoUsuario.this,"Foto actualizada",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
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

                    String imagen = ""+ds.child("imagen").getValue();

                    //cargar foto
                    try{
                        //Si existe iagen en la base de datos
                        Picasso.get().
                                load(imagen)
                                .placeholder(R.drawable.userprefault)
                                .resize(500,500)
                                .into(imgmostrarprimero);
                    }catch (Exception e){
                        //Si no existe imagen en base de datos, mostrar comun
                        Picasso.get().load(R.drawable.userprefault).into(imgmostrarprimero);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }


}