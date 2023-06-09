package com.jhon.gen_dorado_oficial;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.lights.LightState;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Medicamentos extends AppCompatActivity {
    RecyclerView recycler_medicamentos;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;
    //Lista
    List<com.jhon.gen_dorado_oficial.Objetos.Medicamentos> medicamentoList;
    //adaptador
    Medicamentos_adaptador medicamentos_adapter;
    FloatingActionButton floatbutton;
    Toolbar toolbara_medicamento;
    ImageView opps;
    //notificaciones
    private static final String CHANNEL_ID = "canal";

    int horaaplicado,minutoaplicado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);


        //Recycler
        recycler_medicamentos = findViewById(R.id.recycler_medicamentos);
        medicamentoList = new ArrayList<>();
        opps = findViewById(R.id.opps);
        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
        //adaptador
        recycler_medicamentos.setLayoutManager(new LinearLayoutManager(this));
        medicamentos_adapter = new Medicamentos_adaptador(medicamentoList, getApplicationContext());
        recycler_medicamentos.setAdapter(medicamentos_adapter);
        //floatbutto
        floatbutton = findViewById(R.id.floatbuttonmedicamento);
        //modificar color y nombre del toolbar
        toolbara_medicamento = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbara_medicamento);
        getSupportActionBar().setTitle("Medicamentos Registrados");


        //AHORA LLAMAMOS PARA ACTUALIZAR Y EMPEZAR A METER DATOS AL RECYLER
        BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medicamentoList.removeAll(medicamentoList);
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    com.jhon.gen_dorado_oficial.Objetos.Medicamentos medicamento = snapshot.getValue(com.jhon.gen_dorado_oficial.Objetos.Medicamentos.class);
                    medicamentoList.add(medicamento);

                }
                medicamentos_adapter.notifyDataSetChanged();

                if(medicamentoList.isEmpty()){
                    recycler_medicamentos.setVisibility(View.GONE);
                    opps.setVisibility(View.VISIBLE);
                }else{
                    recycler_medicamentos.setVisibility(View.VISIBLE);
                    opps.setVisibility(View.GONE);
                }
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
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setWindowAnimations(R.style.DialogAnimationFromTop);

                Button btnsendmedicamento,intervaloaplicacion;
                EditText nombremedicamento;
                EditText dosismedicamento;


                btnsendmedicamento = dialog.findViewById(R.id.btnsendmedicamento);
                nombremedicamento = dialog.findViewById(R.id.nombremedicamento);
                dosismedicamento = dialog.findViewById(R.id.dosismedicamento);
                intervaloaplicacion = dialog.findViewById(R.id.dosisintervalo);
                //fecha y hora, toca arreglar porque no se obtiene bien el tiempo  java.util
                Calendar fecha = Calendar.getInstance();
                int año = fecha.get(Calendar.YEAR);
                int mes = fecha.get(Calendar.MONTH) + 1;
                int dia = fecha.get(Calendar.DAY_OF_MONTH);
                int hora = fecha.get(Calendar.HOUR_OF_DAY);
                int minuto = fecha.get(Calendar.MINUTE);
                int segundo = fecha.get(Calendar.SECOND);
                Map<String, Integer> fechaR = new HashMap<>();
                fechaR.put("dia", dia);
                fechaR.put("mes", mes);
                fechaR.put("año", año);
                fechaR.put("hora", hora);
                fechaR.put("minuto", minuto);
                fechaR.put("segundo", segundo);
                //time picker dentro

                intervaloaplicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                horaaplicado = hourOfDay;
                                minutoaplicado = minute;
                                intervaloaplicacion.setText(String.format(Locale.getDefault(),"%02d:%02d",horaaplicado,minutoaplicado));
                            }
                        };

                        TimePickerDialog timePickerDialog = new TimePickerDialog(dialog.getContext(),R.style.MyTimePickerStyle, onTimeSetListener,horaaplicado,minutoaplicado,true);
                        timePickerDialog.setTitle("Seleccione el intervalo de aplicación");
                        timePickerDialog.show();
                    }
                });


                //oprimir boton dentro del dialog
                btnsendmedicamento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //verificar que llene la información
                        if (nombremedicamento.getText().toString().isEmpty() || dosismedicamento.getText().toString().isEmpty() || intervaloaplicacion.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Llena toda la información ", Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference medicamentoid = FirebaseDatabase.getInstance().getReference("Medicamentos");
                            String medicamentokey = medicamentoid.push().getKey();
                            String useruid = firebaseAuth.getCurrentUser().getUid();
                            int dosisnum = Integer.parseInt(dosismedicamento.getText().toString());
                            com.jhon.gen_dorado_oficial.Objetos.Medicamentos medicamentos = new com.jhon.gen_dorado_oficial.Objetos.Medicamentos(nombremedicamento.getText().toString(), dosisnum, fechaR,medicamentokey,0,useruid, horaaplicado, minutoaplicado);
                            BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").child(medicamentokey).setValue(medicamentos);
                            dialog.dismiss();
                        }


                    }
                });
                dialog.show();
            }
        });
    }


}