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

import java.util.List;

public class RamasAdapter extends RecyclerView.Adapter<RamasAdapter.RamaViewHolder> {

    public interface OnRamaListener {
        void onEditar(RamaCurso rama);
        void onEliminar(RamaCurso rama);
        void onVerTemas(RamaCurso rama);
    }

    private List<RamaCurso> listaRamas;
    private OnRamaListener listener;

    public RamasAdapter(List<RamaCurso> listaRamas, OnRamaListener listener) {
        this.listaRamas = listaRamas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RamaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rama, parent, false);
        return new RamaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RamaViewHolder holder, int position) {
        RamaCurso rama = listaRamas.get(position);
        holder.tvNombreRama.setText(rama.getNombreRama());

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(rama));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminar(rama));
        holder.btnVerTemas.setOnClickListener(v -> listener.onVerTemas(rama));
    }

    @Override
    public int getItemCount() {
        return listaRamas.size();
    }

    static class RamaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreRama;
        ImageButton btnEditar, btnEliminar, btnVerTemas;

        public RamaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreRama = itemView.findViewById(R.id.tvNombreRama);
            btnEditar = itemView.findViewById(R.id.btnEditarRama);
            btnEliminar = itemView.findViewById(R.id.btnEliminarRama);
            btnVerTemas = itemView.findViewById(R.id.btnVerTemas);
        }
    }
}
