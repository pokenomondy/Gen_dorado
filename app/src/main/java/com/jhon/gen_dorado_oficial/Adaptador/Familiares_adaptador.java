package com.jhon.gen_dorado_oficial.Adaptador;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhon.gen_dorado_oficial.Objetos.Familiares;
import com.jhon.gen_dorado_oficial.Objetos.Medicamentos;
import com.jhon.gen_dorado_oficial.R;

import java.util.ArrayList;
import java.util.List;

public class Familiares_adaptador extends RecyclerView.Adapter<Familiares_adaptador.FamiliaresViewHolder> {

    List<Familiares> familiaresList;

    private Context thisContext;

    public Familiares_adaptador(List<Familiares> familiaresList, Context  context) {
        this.familiaresList = familiaresList;
        this.thisContext = context;

    }

    @NonNull
    @Override
    public FamiliaresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_familiares,parent,false);
        FamiliaresViewHolder holder = new FamiliaresViewHolder(v);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FamiliaresViewHolder holder, int position) {
        Familiares familiares = familiaresList.get(position);
        holder.nombrefamiliar.setText(familiares.getNombrefamiliar());
        holder.apodofamiliar.setText(familiares.getApodofamiliar());

        String uidfamiliar =familiares.getuid();

        //Ponemos adaptador nuevo
        List<Medicamentos> medicamentosList;
        Medicamentos_adaptador medicamentosadaptador;
        medicamentosList = new ArrayList<>();
        holder.recycler_acudientemedicamentos.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

        medicamentosadaptador = new Medicamentos_adaptador(medicamentosList,thiscontext);

        holder.recycler_acudientemedicamentos.setAdapter(medicamentosadaptador);
        //Base de datos para rellenar datos
        DatabaseReference BASE_DE_DATOS,BASE_DE_DATOSDOS;
        FirebaseDatabase firebaseDatabase;
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS");


        BASE_DE_DATOS.child(uidfamiliar).child("Medicamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicamentosList.removeAll(medicamentosList);
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    com.jhon.gen_dorado_oficial.Objetos.Medicamentos medicamento = snapshot.getValue(com.jhon.gen_dorado_oficial.Objetos.Medicamentos.class);
                    medicamentosList.add(medicamento);
                }
                medicamentosadaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }

    @Override
    public int getItemCount() {
        return familiaresList.size();
    }

    public static  class FamiliaresViewHolder extends RecyclerView.ViewHolder{

        TextView nombrefamiliar,apodofamiliar;
        RecyclerView recycler_acudientemedicamentos;

        public FamiliaresViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrefamiliar = itemView.findViewById(R.id.nombrefamiliar_adapter);
            apodofamiliar = itemView.findViewById(R.id.apodofamiliar_adapter);
            recycler_acudientemedicamentos = itemView.findViewById(R.id.recycler_medicamentosacudiente);
        }
    }

}
