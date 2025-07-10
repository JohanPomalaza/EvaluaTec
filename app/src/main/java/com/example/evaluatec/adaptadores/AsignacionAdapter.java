package com.example.evaluatec.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.modelos.AsignacionDto;

import java.util.List;

public class AsignacionAdapter extends RecyclerView.Adapter<AsignacionAdapter.ViewHolder> {

    public interface OnAsignacionActionListener {
        void onEditar(AsignacionDto asignacion);
        void onEliminar(AsignacionDto asignacion);
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
        holder.txtCurso.setText("Curso: " + asignacion.getNombreCurso());
        holder.txtRama.setText("Rama: " + asignacion.getRamaCursoNombre());
        holder.txtGrado.setText("Grado: " + asignacion.getGradoNombre());
        holder.txtSeccion.setText("Sección: " + asignacion.getSeccionNombre());

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(asignacion));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminar(asignacion));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCurso, txtRama, txtGrado, txtSeccion;
        ImageButton btnEditar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCurso = itemView.findViewById(R.id.txtCurso);
            txtRama = itemView.findViewById(R.id.txtRama);
            txtGrado = itemView.findViewById(R.id.txtGrado);
            txtSeccion = itemView.findViewById(R.id.txtSeccion);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
