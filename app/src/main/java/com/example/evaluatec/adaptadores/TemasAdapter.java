package com.example.evaluatec.adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.modelos.TemaEditarDTO;

import java.util.ArrayList;
import java.util.List;

public class TemasAdapter extends RecyclerView.Adapter<TemasAdapter.TemaViewHolder> {
    private List<TemaEditarDTO> listaTemas;
    private final OnTemaActionListener listener;

    public interface OnTemaActionListener {
        void onEditarTema(TemaEditarDTO tema);
        void onEliminarTema(TemaEditarDTO tema);
    }

    public TemasAdapter(List<TemaEditarDTO> listaTemas, OnTemaActionListener listener) {
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
        TemaEditarDTO tema = listaTemas.get(position);
        holder.txtNombre.setText(tema.getNombre());

        holder.btnEditar.setOnClickListener(v -> listener.onEditarTema(tema));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminarTema(tema));
    }

    @Override
    public int getItemCount() {
        return listaTemas != null ? listaTemas.size() : 0;
    }

    public static class TemaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        ImageButton btnEditar, btnEliminar;

        public TemaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreTema);
            btnEditar = itemView.findViewById(R.id.btnEditarTema);
            btnEliminar = itemView.findViewById(R.id.btnEliminarTema);
        }
    }
}
