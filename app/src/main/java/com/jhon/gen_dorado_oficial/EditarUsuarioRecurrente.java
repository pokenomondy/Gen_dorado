package com.jhon.gen_dorado_oficial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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

    //Fotos
    CircleImageView imgperfilshow;
    ImageView editbtnfotorecurrente;
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

        //cargar imagen
        imgperfilshow = findViewById(R.id.imgperfilshow);


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
                Intent intent = new Intent(EditarUsuarioRecurrente.this,DashboardUser.class);
                startActivity(intent);

            }
        });

        //imagen editar
        storageReference = FirebaseStorage.getInstance().getReference();
        editbtnfotorecurrente = findViewById(R.id.editbtnfotorecurrente);
        progressDialog = new ProgressDialog(this);

        editbtnfotorecurrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
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
                    String imagen = ""+ds.child("imagen").getValue();

                    //cargar foto
                    try{
                        //Si existe iagen en la base de datos
                        Picasso.get().
                                load(imagen)
                                .placeholder(R.drawable.userprefault)
                                .resize(500,500)
                                .into(imgperfilshow);
                    }catch (Exception e){
                        //Si no existe imagen en base de datos, mostrar comun
                        Picasso.get().load(R.drawable.userprefault).into(imgperfilshow);
                    }

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

    private void subirPhoto(Uri image_url) {
        Toast.makeText(EditarUsuarioRecurrente.this,"Subimos foto",Toast.LENGTH_SHORT).show();
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
                            BASE_DE_DATOS.child(firebaseUser.getUid()).updateChildren(map);
                            Toast.makeText(EditarUsuarioRecurrente.this,"Foto actualizada",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}