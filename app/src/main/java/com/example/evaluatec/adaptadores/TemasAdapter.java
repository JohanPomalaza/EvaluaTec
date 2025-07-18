package com.example.evaluatec.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.modelos.RamaCurso;
import com.example.evaluatec.modelos.TemaCurso;

import java.util.ArrayList;
import java.util.List;

public class TemasAdapter extends RecyclerView.Adapter<TemasAdapter.TemaViewHolder> {
    private List<TemaCurso> listaTemas;
    private final OnTemaActionListener listener;

    public interface OnTemaActionListener {
        void onEditarTema(TemaCurso tema);
        void onEliminarTema(TemaCurso tema);
        void onVerHistorialTemas(TemaCurso tema);
    }

    public TemasAdapter(List<TemaCurso> listaTemas, OnTemaActionListener listener) {
        this.listaTemas = listaTemas != null ? listaTemas : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public TemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tema, parent, false);
        return new TemaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TemaViewHolder holder, int position) {
        TemaCurso tema = listaTemas.get(position);
        holder.txtNombre.setText(tema.getNombre());

        holder.btnEditar.setOnClickListener(v -> listener.onEditarTema(tema));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminarTema(tema));
        holder.btnVerHistorial.setOnClickListener(v -> listener.onVerHistorialTemas(tema));
    }

    @Override
    public int getItemCount() {
        return listaTemas != null ? listaTemas.size() : 0;
    }

    public static class TemaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        ImageButton btnEditar, btnEliminar,btnVerHistorial;

        public TemaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreTema);
            btnEditar = itemView.findViewById(R.id.btnEditarTema);
            btnEliminar = itemView.findViewById(R.id.btnEliminarTema);
            btnVerHistorial = itemView.findViewById(R.id.btnHistorialTema);
        }
    }
}
