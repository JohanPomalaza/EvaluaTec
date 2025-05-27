package com.example.evaluatec.pantallas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.modelos.Nota;
import com.example.evaluatec.modelos.NotaPorCurso;

import java.util.List;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {
    private List<NotaPorCurso> listaNotas;

    public NotaAdapter(List<NotaPorCurso> listaNotas) {
        this.listaNotas = listaNotas;
    }
    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso_detalle, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        NotaPorCurso nota = listaNotas.get(position);

        holder.tvNombreTema.setText("" + (nota.getTema() != null ? nota.getTema() : "Sin Tema"));
        holder.tvNota.setText("Nota: " + String.valueOf(nota.getNota()));
        holder.tvNombreRama.setText("" + (nota.getRama() != null ? nota.getRama() : "Sin Rama"));
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public void actualizarLista(List<NotaPorCurso> nuevasNotas) {
        this.listaNotas = nuevasNotas;
        notifyDataSetChanged();
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreTema, tvNota, tvNombreRama;

        NotaViewHolder(View itemView) {
            super(itemView);
            tvNombreTema = itemView.findViewById(R.id.tvNombreTema);
            tvNota = itemView.findViewById(R.id.tvNota);
            tvNombreRama = itemView.findViewById(R.id.tvNombreRama);
        }
    }
}
