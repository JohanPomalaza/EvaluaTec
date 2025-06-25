package com.example.evaluatec.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.modelos.EstudianteDto;

import java.util.List;

public class EstudianteManteAdapter extends RecyclerView.Adapter<EstudianteManteAdapter.ViewHolder> {
    private List<EstudianteDto> estudiantes;
    private OnEstudianteListener listener;

    public interface OnEstudianteListener {
        void onEditar(EstudianteDto estudiante);
        void onAsignarGrado(EstudianteDto estudiante);
    }

    public EstudianteManteAdapter(List<EstudianteDto> estudiantes, OnEstudianteListener listener) {
        this.estudiantes = estudiantes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estudiante_gestion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EstudianteDto estudiante = estudiantes.get(position);
        holder.txtNombre.setText(estudiante.getNombre() + " " + estudiante.getApellido());
        if (estudiante.getGrado() != null && !estudiante.getGrado().isEmpty()) {
            holder.txtGrado.setText("Grado: " + estudiante.getGrado());
        } else {
            holder.txtGrado.setText("Grado: No asignado");
        }
        holder.btnEditar.setOnClickListener(v -> listener.onEditar(estudiante));
        holder.btnAsignarGrado.setOnClickListener(v -> listener.onAsignarGrado(estudiante));
    }

    @Override
    public int getItemCount() {
        return estudiantes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre,txtGrado;
        Button btnEditar, btnAsignarGrado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnAsignarGrado = itemView.findViewById(R.id.btnAsignarGrado);
            txtGrado = itemView.findViewById(R.id.txtGrado);
        }
    }
}
