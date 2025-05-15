package com.example.evaluatec.pantallas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.modelos.Estudiante;

import java.util.List;

public class EstudianteAdapter extends RecyclerView.Adapter<EstudianteAdapter.EstudianteViewHolder> {

    public interface OnEstudianteClickListener {
        void onEstudianteClick(Estudiante estudiante);
    }

    private List<Estudiante> estudiantes;
    private OnEstudianteClickListener listener;


    public EstudianteAdapter(List<Estudiante> estudiantes, OnEstudianteClickListener listener) {
        this.estudiantes = estudiantes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EstudianteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_estudiante, parent, false);
        return new EstudianteViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull EstudianteViewHolder holder, int position) {
        Estudiante estudiante = estudiantes.get(position);
        holder.bind(estudiante, listener);
    }
    @Override
    public int getItemCount() {
        return estudiantes.size();
    }
    static class EstudianteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCompleto;

        public EstudianteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCompleto = itemView.findViewById(R.id.textNombreEstudiante);
        }

        public void bind(final Estudiante estudiante, final OnEstudianteClickListener listener) {
            String nombreCompleto = estudiante.getNombre() + " " + estudiante.getApellido();
            tvNombreCompleto.setText(nombreCompleto);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEstudianteClick(estudiante);
                }
            });
        }
    }
    }
