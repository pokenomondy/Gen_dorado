    package com.jhon.gen_dorado_oficial.Adaptador;

    import android.app.Dialog;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.jhon.gen_dorado_oficial.Objetos.Historialmedicamento;
    import com.jhon.gen_dorado_oficial.Objetos.Medicamentos;
    import com.jhon.gen_dorado_oficial.R;

    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class Medicamentos_adaptador extends RecyclerView.Adapter<Medicamentos_adaptador.MedicamentosViewHolder> {

        List<com.jhon.gen_dorado_oficial.Objetos.Medicamentos> medicamentos;

        public Medicamentos_adaptador(List<Medicamentos> medicamentos) {
            this.medicamentos = medicamentos;
        }

        @NonNull
        @Override
        public MedicamentosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_medicamento,parent,false);
            MedicamentosViewHolder holder = new MedicamentosViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MedicamentosViewHolder holder, int position) {
            Medicamentos medicamento = medicamentos.get(position);
            holder.medicamentonombre.setText(medicamento.getMedicamento());
            // Se tiene que obtener, el valor del string en este INT, para que se setee el texto
            holder.dosisintervalo.setText(medicamento.getIntervaloaplicacion()+"hr");
            holder.horaaplicacion.setText(medicamento.getHorario());
            holder.siguientemedicamento.setText(medicamento.getCalcsigmedicamento());

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
            String intervalo = medicamento.getIntervaloaplicacion();

            //icon con historial
            holder.icon_historial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Historialmedicamento> historialmedicamentoList;
                    historialmedicamentoList = new ArrayList<>();
                    Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.item_historial_medicamento);
                    RecyclerView recycler_historial = dialog.findViewById(R.id.recycler_historial);
                    recycler_historial.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
                    Historial_adaptador historial_adaptador;
                    historial_adaptador = new Historial_adaptador(historialmedicamentoList);
                    recycler_historial.setAdapter(historial_adaptador);
                    Toast.makeText(v.getContext(),"Sirve",Toast.LENGTH_SHORT).show();
                    //inicializamos de nuevo bases de datos
                    DatabaseReference BASE_DE_DATOS,BASE_DE_DATOSDOS;
                    FirebaseDatabase firebaseDatabase;
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
                    BASE_DE_DATOSDOS = firebaseDatabase.getReference().getRoot();


                    BASE_DE_DATOSDOS.child("Historial").child(firebaseAuth.getCurrentUser().getUid()).child("Historialmedicamento").child(medicamentoId).addValueEventListener(new ValueEventListener() {

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


                    dialog.show();


                }
            });
            //icon de delete
            holder.icon_delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //obtener posición
                    DatabaseReference BASE_DE_DATOS;
                    FirebaseDatabase firebaseDatabase;
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
                    DatabaseReference medicamentoREF = BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").child(medicamentoId);
                    medicamentoREF.removeValue();
                    Toast.makeText(v.getContext(), medicamentoId, Toast.LENGTH_SHORT).show();

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
                    Button btnsendmedicamento,btneliminarmedicamento;
                    EditText nombremedicamento;
                    EditText dosismedicamento, intervaloaplicacion;

                    btnsendmedicamento = dialog.findViewById(R.id.btnsendmedicamento);
                    nombremedicamento = dialog.findViewById(R.id.nombremedicamento);
                    dosismedicamento = dialog.findViewById(R.id.dosismedicamento);
                    intervaloaplicacion = dialog.findViewById(R.id.dosisintervalo);
                    btneliminarmedicamento = dialog.findViewById(R.id.btneliminarmedicamento);

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

                    //btn actualizar datos
                    btnsendmedicamento.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int numtomado = Integer.parseInt(String.valueOf(medicamento.getNum_tomado()));
                            //convertir a numero la dosis
                            String medicamentosid = medicamento.getId();
                            int dosisnum = Integer.parseInt(dosismedicamento.getText().toString());
                            com.jhon.gen_dorado_oficial.Objetos.Medicamentos medicamentos = new com.jhon.gen_dorado_oficial.Objetos.Medicamentos(nombremedicamento.getText().toString(), dosisnum, fechaR, intervaloaplicacion.getText().toString(),medicamentosid,numtomado);
                            BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").child(medicamentosid).setValue(medicamentos);
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
                            Toast.makeText(v.getContext(), medicamentoId, Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });
            //icon de registrar tomado
            holder.icn_registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"sumar",Toast.LENGTH_SHORT).show();
                    FirebaseAuth firebaseAuth;
                    FirebaseDatabase firebaseDatabase;
                    DatabaseReference BASE_DE_DATOS,BASE_DE_DATOSDOS;
                    //Firebase
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
                    BASE_DE_DATOSDOS = firebaseDatabase.getReference().getRoot();
                    String calcsiguiente = medicamento.getCalcsigmedicamento();


                    BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").child(medicamentoId).child("num_tomado").setValue(numnuevotomado);
                    //REGISTRAR NUEVO MEDICAMENTO TOMADO
                    Historialmedicamento historialmedicamento = new Historialmedicamento(numnuevotomado,"2",calcsiguiente);
                    BASE_DE_DATOSDOS.child("Historial").child(firebaseAuth.getCurrentUser().getUid()).child("Historialmedicamento").child(medicamentoId).push().setValue(historialmedicamento);

                    if (numnuevotomado==dosisactual){

                        firebaseDatabase = FirebaseDatabase.getInstance();
                        String medicamentoId = medicamento.getId();
                        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");
                        DatabaseReference medicamentoREF = BASE_DE_DATOS.child(firebaseAuth.getCurrentUser().getUid()).child("Medicamentos").child(medicamentoId);
                        medicamentoREF.removeValue();
                        Toast.makeText(v.getContext(), medicamentoId, Toast.LENGTH_SHORT).show();
                    }


                }


            });
        }




        @Override
        public int getItemCount() {
            return medicamentos.size();
        }

        public static class MedicamentosViewHolder extends RecyclerView.ViewHolder{
            TextView dosisintervalo,medicamentonombre,horaaplicacion,siguientemedicamento;
            ImageView icon_delete, icon_edit,icn_registrar,icon_historial;
            List<Historialmedicamento> historialmedicamentoList;
            Historial_adaptador historial_adaptador;
            public MedicamentosViewHolder(@NonNull View itemView) {
                super(itemView);
                dosisintervalo = itemView.findViewById(R.id.dosisintervalo);
                medicamentonombre = itemView.findViewById(R.id.medicamentonombre);
                horaaplicacion = itemView.findViewById(R.id.horaaplicacion);
                siguientemedicamento = itemView.findViewById(R.id.siguientemedicamento);
                icon_delete = itemView.findViewById(R.id.icon_delete);
                icon_edit = itemView.findViewById(R.id.icon_edit);
                icn_registrar= itemView.findViewById(R.id.icn_registrar);
                icon_historial = itemView.findViewById(R.id.icon_historial);
               //recycler y adaptador
                historialmedicamentoList = new ArrayList<>();

            }

        }
    }
