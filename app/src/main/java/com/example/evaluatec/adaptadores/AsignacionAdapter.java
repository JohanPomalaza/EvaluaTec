package com.example.evaluatec.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.modelos.AsignacionDto;

import java.util.List;

public class AsignacionAdapter extends RecyclerView.Adapter<AsignacionAdapter.ViewHolder> {

    public interface OnAsignacionActionListener {
        void onEditar(AsignacionDto asignacion);
    }

    private List<AsignacionDto> lista;
    private OnAsignacionActionListener listener;

    public AsignacionAdapter(List<AsignacionDto> lista, OnAsignacionActionListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asignacion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AsignacionDto asignacion = lista.get(position);
        holder.txtCurso.setText("Curso: " + asignacion.getRamaCursoNombre());
        holder.txtGrado.setText("Grado: " + asignacion.getGradoNombre());

        holder.itemView.setOnClickListener(v -> listener.onEditar(asignacion));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCurso, txtGrado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCurso = itemView.findViewById(R.id.txtCurso);
            txtGrado = itemView.findViewById(R.id.txtGrado);
        }
    }
}
