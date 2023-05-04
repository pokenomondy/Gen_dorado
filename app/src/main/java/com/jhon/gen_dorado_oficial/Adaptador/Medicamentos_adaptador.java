package com.jhon.gen_dorado_oficial.Adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jhon.gen_dorado_oficial.Objetos.Medicamentos;
import com.jhon.gen_dorado_oficial.R;

import java.util.List;

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
        holder.dosisintervalo.setText(medicamento.getIntervaloaplicacion());
        holder.horaaplicacion.setText(medicamento.getHorario().toString());
        holder.siguientemedicamento.setText(medicamento.getCalcsigmedicamento());
    }

    @Override
    public int getItemCount() {
        return medicamentos.size();
    }

    public static class MedicamentosViewHolder extends RecyclerView.ViewHolder{
        TextView dosisintervalo,medicamentonombre,horaaplicacion,siguientemedicamento;
        public MedicamentosViewHolder(@NonNull View itemView) {
            super(itemView);
            dosisintervalo = itemView.findViewById(R.id.dosisintervalo);
            medicamentonombre = itemView.findViewById(R.id.medicamentonombre);
            horaaplicacion = itemView.findViewById(R.id.horaaplicacion);
            siguientemedicamento = itemView.findViewById(R.id.siguientemedicamento);
        }
    }
}
