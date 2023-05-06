    package com.jhon.gen_dorado_oficial.Adaptador;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
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
            holder.dosisintervalo.setText(medicamento.getIntervaloaplicacion()+"hr");
            holder.horaaplicacion.setText(medicamento.getHorario());
            holder.siguientemedicamento.setText(medicamento.getCalcsigmedicamento());

            //icon de delete
            holder.icon_delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //obtener posición
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

        @Override
        public int getItemCount() {
            return medicamentos.size();
        }

        public static class MedicamentosViewHolder extends RecyclerView.ViewHolder{
            TextView dosisintervalo,medicamentonombre,horaaplicacion,siguientemedicamento;
            ImageView icon_delete;
            public MedicamentosViewHolder(@NonNull View itemView) {
                super(itemView);
                dosisintervalo = itemView.findViewById(R.id.dosisintervalo);
                medicamentonombre = itemView.findViewById(R.id.medicamentonombre);
                horaaplicacion = itemView.findViewById(R.id.horaaplicacion);
                siguientemedicamento = itemView.findViewById(R.id.siguientemedicamento);
                icon_delete = itemView.findViewById(R.id.icon_delete);
            }

        }
    }
