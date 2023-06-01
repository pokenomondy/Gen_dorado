    package com.jhon.gen_dorado_oficial.Adaptador;

    import android.app.Dialog;
    import android.app.TimePickerDialog;
    import android.content.Context;
    import android.graphics.Color;
    import android.graphics.drawable.ColorDrawable;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.TimePicker;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.work.Data;
    import androidx.work.WorkManager;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.Query;
    import com.google.firebase.database.ValueEventListener;
    import com.jhon.gen_dorado_oficial.Objetos.Historialmedicamento;
    import com.jhon.gen_dorado_oficial.Objetos.Medicamentos;
    import com.jhon.gen_dorado_oficial.R;
    import com.jhon.gen_dorado_oficial.servicios.workerService;

    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Locale;
    import java.util.Map;

    public class Medicamentos_adaptador extends RecyclerView.Adapter<Medicamentos_adaptador.MedicamentosViewHolder> {

        List<com.jhon.gen_dorado_oficial.Objetos.Medicamentos> medicamentos;
        private Context thisContext;

        public Medicamentos_adaptador(List<Medicamentos> medicamentos, Context context) {
            this.medicamentos = medicamentos;
            this.thisContext = context;
        }

        @NonNull
        @Override
        public MedicamentosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_medicamento,parent,false);
            MedicamentosViewHolder holder = new MedicamentosViewHolder(v);
            return holder;
        }


        //FUNCIONES NOTIFICADOR
        private Data guardarData(String titulo, String detalle, int idNoti){
            return new Data.Builder()
                    .putString("titulo",titulo)
                    .putString("texto",detalle)
                    .putInt("idNoti",idNoti)
                    .build();
        }

        public void eliminarNoti(String tag){
            WorkManager.getInstance(thisContext).cancelAllWorkByTag(tag);
        }
        // FIN FUNCIONES NOTIFICADOR

        public void reset() {
            medicamentos.clear(); // Borra la lista de datos
            notifyDataSetChanged(); // Notifica al adapter de los cambios
        }


        @Override
        public void onBindViewHolder(@NonNull MedicamentosViewHolder holder, int position) {
            Medicamentos medicamento = medicamentos.get(position);
            holder.medicamentonombre.setText(medicamento.getMedicamento());
            // Se tiene que obtener, el valor del string en este INT, para que se setee el texto
            holder.dosisintervalo.setText(medicamento.getHora()+":"+medicamento.getMinuto()+"hrs");
            holder.horaaplicacion.setText(medicamento.getHorario());
            holder.siguientemedicamento.setText(medicamento.getCalcsigmedicamento());

            //verificar si es paciente o acudiente
            //Firebase
            FirebaseAuth firebaseAuth;
            FirebaseUser firebaseUser;
            DatabaseReference BASE_DE_DATOS;
            FirebaseDatabase firebaseDatabase;
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();
            BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
            Query query = BASE_DE_DATOS.orderByChild("Numero de celular").equalTo(firebaseUser.getPhoneNumber());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //iterar para buscar por numero de celular
                    for (DataSnapshot ds : snapshot.getChildren()){
                        //OBTENER VALORES DE BASE DA DATOS
                        String rol = ""+ds.child("rol").getValue();

                        if (rol.equals("Paciente")){
                            holder.icon_historial.setVisibility(View.INVISIBLE);
                        }else {
                            holder.icon_edit.setVisibility(View.INVISIBLE);
                            holder.icn_registrar.setVisibility(View.INVISIBLE);
                            holder.icon_delete.setVisibility(View.INVISIBLE);
                            holder.tomadoexpirado.setVisibility(View.INVISIBLE);
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }


            });

            String uidusuario = medicamento.getUiseruid();
            //NOTIFICADOR
            long time = medicamento.obtenerMillis();
            String key = medicamento.getMedicamento()+medicamento.getNum_tomado()+medicamento.getId();
            Data data = guardarData( medicamento.getMedicamento() + " - " + medicamento.getHorario(), "Es hora de tomar tu medicamento", medicamento.getNum_tomado());
            workerService.guardarNoti(time, data, key);

            //FIN NOTIFICADOR

           //Variables par usar
            String medicamentoId = medicamento.getId();
            //variables
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
            //termina fecha
            int numtomado = Integer.parseInt(String.valueOf(medicamento.getNum_tomado()));
            int numnuevotomado = numtomado+1;
            int dosisactual = medicamento.getDosis();
            String nombremedimcaneto = medicamento.getMedicamento();
            int intervalo = medicamento.getHora();

            //Tomado expirado
            if (dosisactual==medicamento.getNum_tomado()){

                Query verificar = BASE_DE_DATOS.orderByChild("Numero de celular").equalTo(firebaseUser.getPhoneNumber());
                verificar.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //iterar para buscar por numero de celular
                        for (DataSnapshot ds : snapshot.getChildren()){
                            //OBTENER VALORES DE BASE DA DATOS
                            String rol = ""+ds.child("rol").getValue();

                            if (rol.equals("Paciente")){
                                holder.tomadoexpirado.setVisibility(View.VISIBLE);
                                holder.icon_edit.setVisibility(View.GONE);
                                holder.icn_registrar.setVisibility(View.GONE);
                                holder.icon_delete.setVisibility(View.VISIBLE);
                            }else {

                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });

                holder.icon_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase firebaseDatabase;
                        DatabaseReference BASE_DE_DATOS;
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        String medicamentoId = medicamento.getId();
                        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
                        DatabaseReference medicamentoREF = BASE_DE_DATOS.child(firebaseUser.getUid()).child("Medicamentos").child(medicamentoId);
                        medicamentoREF.removeValue();
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, medicamentos.size());
                    }
                });
            }
            //icon con historial  - > hay que llamar familiares, y cuando sea un acudiente se llame lista desde uid familiar
            holder.icon_historial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Historialmedicamento> historialmedicamentoList;
                    historialmedicamentoList = new ArrayList<>();
                    Dialog dialog = new Dialog(v.getContext());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    //Variables del dialog
                    dialog.setContentView(R.layout.item_historial_medicamento);
                    TextView dosisintervalohistorial = dialog.findViewById(R.id.dosisintervalohistorial);
                    TextView medicamentonombrehistorial = dialog.findViewById(R.id.medicamentonombrehistorial);
                    RecyclerView recycler_historial = dialog.findViewById(R.id.recycler_historial);
                    recycler_historial.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
                    Historial_adaptador historial_adaptador;
                    historial_adaptador = new Historial_adaptador(historialmedicamentoList);
                    recycler_historial.setAdapter(historial_adaptador);

                    dosisintervalohistorial.setText(medicamento.getHora()+":"+medicamento.getMinuto());
                    medicamentonombrehistorial.setText(medicamento.getMedicamento());


                    //inicializamos de nuevo bases de datos
                    DatabaseReference BASE_DE_DATOS,BASE_DE_DATOSDOS;
                    FirebaseDatabase firebaseDatabase;
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
                    BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
                    BASE_DE_DATOS.child(uidusuario).child("Medicamentos").child(medicamentoId).child("Historialmedicamento").addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Lista , por que no sale?
                            historialmedicamentoList.clear();
                            for (DataSnapshot snapshot :
                                    dataSnapshot.getChildren()) {
                                Historialmedicamento medicamento = snapshot.getValue(Historialmedicamento.class);
                                historialmedicamentoList.add(medicamento);
                                Log.d("TAG", "Agregado medicamento: " + medicamento);
                            }

                            historial_adaptador.notifyDataSetChanged();

                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //fin separación




                    dialog.show();


                }
            });
            //icon de editar
            holder.icon_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth firebaseAuth;
                    FirebaseUser firebaseUser;
                    FirebaseDatabase firebaseDatabase;
                    DatabaseReference BASE_DE_DATOS;
                    //Firebase
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseUser = firebaseAuth.getCurrentUser();
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
                    Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.item_add_medicamento);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button btnsendmedicamento,btneliminarmedicamento,intervaloaplicacion;
                    EditText nombremedicamento;
                    EditText dosismedicamento;

                    btnsendmedicamento = dialog.findViewById(R.id.btnsendmedicamento);
                    nombremedicamento = dialog.findViewById(R.id.nombremedicamento);
                    dosismedicamento = dialog.findViewById(R.id.dosismedicamento);
                    intervaloaplicacion = dialog.findViewById(R.id.dosisintervalo);
                    btneliminarmedicamento = dialog.findViewById(R.id.btneliminarmedicamento);
                    btneliminarmedicamento.setVisibility(View.VISIBLE);

                    //fecha y hora, toca arreglar porque no se obtiene bien el tiempo  java.util
                    Calendar fecha = Calendar.getInstance();
                    int año = fecha.get(Calendar.YEAR);
                    int mes = fecha.get(Calendar.MONTH) + 1;
                    int dia = fecha.get(Calendar.DAY_OF_MONTH);
                    int hora = fecha.get(Calendar.HOUR_OF_DAY);
                    int minuto = fecha.get(Calendar.MINUTE);
                    int segundo = fecha.get(Calendar.SECOND);
                    //oprimir boton dentro del dialog
                    Map<String, Integer> fechaR = new HashMap<>();
                    fechaR.put("dia", dia);
                    fechaR.put("mes", mes);
                    fechaR.put("año", año);
                    fechaR.put("hora", hora);
                    fechaR.put("minuto", minuto);
                    fechaR.put("segundo", segundo);

                    //hora y minutos
                    final int[] horaaplicado = new int[1];
                    final int[] minutoaplicado = new int[1];

                    intervaloaplicacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    horaaplicado[0] = hourOfDay;
                                    minutoaplicado[0] = minute;
                                    intervaloaplicacion.setText(String.format(Locale.getDefault(),"%02d:%02d", horaaplicado[0], minutoaplicado[0]));
                                }
                            };

                            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),R.style.MyTimePickerStyle, onTimeSetListener, horaaplicado[0], minutoaplicado[0],true);
                            timePickerDialog.setTitle("Seleccione el intervalo de aplicación");
                            timePickerDialog.show();
                        }
                    });


                    //btn actualizar datos
                    btnsendmedicamento.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String medicamentosid = medicamento.getId();
                            String namemedicamento = nombremedicamento.getText().toString();
                            //convertir a numero la dosis
                            int dosisnum = 0;
                            String interaplica = intervaloaplicacion.getText().toString();
                            //ifs para conger los valores que se pueden editar
                            if (namemedicamento.equals("")){
                                namemedicamento = medicamento.getMedicamento();
                            }
                            if (dosismedicamento.getText().toString().equals("")){
                                dosisnum = medicamento.getDosis();
                            }else{
                                dosisnum = Integer.parseInt(dosismedicamento.getText().toString());
                            }
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("dosis", dosisnum);
                            updates.put("medicamento",namemedicamento);
                            updates.put("intervaloaplicacion",interaplica);
                            updates.put("hora",horaaplicado[0]);
                            updates.put("minuto",minutoaplicado[0]);

                            //poner dosis num
                            BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").child(medicamentoId).updateChildren(updates);






                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                    //btn eliminar

                    btneliminarmedicamento.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference BASE_DE_DATOS;
                            FirebaseDatabase firebaseDatabase;
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            String medicamentoId = medicamento.getId();
                            BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
                            DatabaseReference medicamentoREF = BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").child(medicamentoId);
                            medicamentoREF.removeValue();
                            dialog.dismiss();
                        }
                    });


                }
            });
            //icon de registrar tomado
            holder.icn_registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Notificacion ACTUALIZAR
                    eliminarNoti(medicamento.getMedicamento()+medicamento.getNum_tomado()+medicamento.getId());
                    //FIN NOTIFICACION

                    Dialog dialogregistro = new Dialog(v.getContext());
                    dialogregistro.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogregistro.setContentView(R.layout.item_add_registrotomado);
                    Button btn_send_registro;
                    btn_send_registro = dialogregistro.findViewById(R.id.btn_send_registro);

                    btn_send_registro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseAuth firebaseAuth;
                            FirebaseDatabase firebaseDatabase;
                            DatabaseReference BASE_DE_DATOS,BASE_DE_DATOSDOS;
                            //Firebase
                            firebaseAuth = FirebaseAuth.getInstance();
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
                            BASE_DE_DATOSDOS = firebaseDatabase.getReference().getRoot();
                            String calcsiguiente = medicamento.getCalcTomado();
                            String horaTomada = medicamento.hora_tomada_String();
                            BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").child(medicamentoId).child("num_tomado").setValue(numnuevotomado);
                            BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").child(medicamentoId).child("fechainicio").setValue(fechaR);



                            //REGISTRAR NUEVO MEDICAMENTO TOMADO
                            Historialmedicamento historialmedicamento = new Historialmedicamento(numnuevotomado,horaTomada,calcsiguiente);
                            Toast.makeText(v.getContext(),"Dosis registrada correctamente",Toast.LENGTH_SHORT).show();



                            BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").child(medicamentoId).child("Historialmedicamento").push().setValue(historialmedicamento);


                            dialogregistro.dismiss();

                        }

                    });
                    dialogregistro.show();




                }


            });
        }




        @Override
        public int getItemCount() {
            return medicamentos.size();
        }

        public static class MedicamentosViewHolder extends RecyclerView.ViewHolder{
            TextView dosisintervalo,medicamentonombre,horaaplicacion,siguientemedicamento,tomadoexpirado;
            ImageView icon_edit,icn_registrar,icon_historial,icon_delete;
            List<Historialmedicamento> historialmedicamentoList;
            Historial_adaptador historial_adaptador;
            public MedicamentosViewHolder(@NonNull View itemView) {
                super(itemView);
                dosisintervalo = itemView.findViewById(R.id.dosisintervalo);
                medicamentonombre = itemView.findViewById(R.id.medicamentonombre);
                horaaplicacion = itemView.findViewById(R.id.horaaplicacion);
                siguientemedicamento = itemView.findViewById(R.id.siguientemedicamento);
                icon_edit = itemView.findViewById(R.id.icon_edit);
                icn_registrar= itemView.findViewById(R.id.icn_registrar);
                icon_historial = itemView.findViewById(R.id.icon_historial);
                tomadoexpirado = itemView.findViewById(R.id.tomadoexpirado);
                icon_delete = itemView.findViewById(R.id.icon_delete);

               //recycler y adaptador
                historialmedicamentoList = new ArrayList<>();

            }

        }
    }
