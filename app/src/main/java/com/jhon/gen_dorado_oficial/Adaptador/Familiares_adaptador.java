package com.jhon.gen_dorado_oficial.Adaptador;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jhon.gen_dorado_oficial.Objetos.Familiares;
import com.jhon.gen_dorado_oficial.R;

import java.util.List;

public class Familiares_adaptador extends RecyclerView.Adapter<Familiares_adaptador.FamiliaresViewHolder> {

    List<Familiares> familiaresList;

    public Familiares_adaptador(List<Familiares> familiaresList) {
        this.familiaresList = familiaresList;
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
    }

    @Override
    public int getItemCount() {
        return familiaresList.size();
    }

    public static  class FamiliaresViewHolder extends RecyclerView.ViewHolder{

        TextView nombrefamiliar,apodofamiliar;

        public FamiliaresViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrefamiliar = itemView.findViewById(R.id.nombrefamiliar_adapter);
            apodofamiliar = itemView.findViewById(R.id.apodofamiliar_adapter);
        }
    }

}
