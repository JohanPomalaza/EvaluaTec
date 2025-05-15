package com.example.evaluatec.pantallas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.modelos.Nota;

import java.util.List;
import java.util.Map;

public class NotaAdapterDocente extends RecyclerView.Adapter<NotaAdapterDocente.NotaViewHolder> {
    private List<Nota> listaNotas;
    private int idUsuarioEstudiante;
    private OnNotaEditListener onNotaEditListener;

    public interface OnNotaEditListener {
        void onNotaEdit(Nota nota, int idUsuarioEstudiante);
    }

    public NotaAdapterDocente(List<Nota> listaNotas, int idUsuarioEstudiante, OnNotaEditListener onNotaEditListener) {
        this.listaNotas = listaNotas;
        this.idUsuarioEstudiante = idUsuarioEstudiante;
        this.onNotaEditListener = onNotaEditListener;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Nota nota = listaNotas.get(position);
        holder.tvNombreTema.setText("" + (nota.getTema() != null ? nota.getTema() : "Sin Tema"));
        holder.textNota.setText(String.valueOf(nota.getNota()));

        holder.btnEditarNota.setOnClickListener(v -> {
            if (onNotaEditListener != null) {
                onNotaEditListener.onNotaEdit(nota, idUsuarioEstudiante);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreTema, textNota;
        Button btnEditarNota;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreTema = itemView.findViewById(R.id.tvNombreTema);
            textNota = itemView.findViewById(R.id.textNota);
            btnEditarNota = itemView.findViewById(R.id.btnEditarNota);
        }
    }

}
