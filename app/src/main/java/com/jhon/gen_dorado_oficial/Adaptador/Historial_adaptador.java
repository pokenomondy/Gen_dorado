package com.jhon.gen_dorado_oficial.Adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jhon.gen_dorado_oficial.Objetos.Familiares;
import com.jhon.gen_dorado_oficial.Objetos.Historialmedicamento;
import com.jhon.gen_dorado_oficial.Objetos.Medicamentos;
import com.jhon.gen_dorado_oficial.R;

import java.util.List;

public class Historial_adaptador extends RecyclerView.Adapter<Historial_adaptador.Historial_adaptadorViewHolder> {

    List<Historialmedicamento> historialmedicamentos;

    public Historial_adaptador(List<Historialmedicamento> historialmedicamentos) {
        this.historialmedicamentos = historialmedicamentos;
    }

    @NonNull
    @Override
    public Historial_adaptadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_historial_medicamento,parent,false);
        Historial_adaptador.Historial_adaptadorViewHolder holder = new Historial_adaptador.Historial_adaptadorViewHolder(v);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Historial_adaptadorViewHolder holder, int position) {
        //Poner datos de Date
        Historialmedicamento medicamentos = historialmedicamentos.get(position);
        //Poner datos de Medicaentos
        holder.historialdosis.setText((String.valueOf(medicamentos.getNumdosis())));
        holder.historialtomado.setText(medicamentos.getHoratomada().toString());
        holder.historialcalcsiguiente.setText(medicamentos.getCalculotomado().toString());
    }

    @Override
    public int getItemCount() {
        return historialmedicamentos.size();
    }

    public static  class Historial_adaptadorViewHolder extends RecyclerView.ViewHolder {

        TextView historialdosis,historialtomado,historialcalcsiguiente;

        public Historial_adaptadorViewHolder(@NonNull View itemView) {
            super(itemView);
            historialdosis = itemView.findViewById(R.id.historialdosis);
            historialtomado = itemView.findViewById(R.id.historialtomado);
            historialcalcsiguiente = itemView.findViewById(R.id.historialcalcsiguiente);

        }
    }


}



