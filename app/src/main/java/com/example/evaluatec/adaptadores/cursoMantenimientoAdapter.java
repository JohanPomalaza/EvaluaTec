package com.example.evaluatec.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.modelos.CursoMantenimiento;

import java.util.List;

public class cursoMantenimientoAdapter extends RecyclerView.Adapter<cursoMantenimientoAdapter.ViewHolder> {
    private List<CursoMantenimiento> lista;
    private OnCursoListener listener;
    private int usuarioId;


    public interface OnCursoListener {
        void onEditar(CursoMantenimiento cursoMante, int usuarioId);
        void onEliminar(CursoMantenimiento cursoMante, int usuarioId);
        void onVerRamasYTemas(CursoMantenimiento cursoMante, int usuarioId);

        void onVerHistorial(CursoMantenimiento cursoMante, int usuarioId);
    }

    public cursoMantenimientoAdapter(List<CursoMantenimiento> lista, OnCursoListener listener, int usuarioId) {
        this.lista = lista;
        this.listener = listener;
        this.usuarioId = usuarioId;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso_mantenimiento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CursoMantenimiento cursoMante = lista.get(position);
        holder.txtNombre.setText(cursoMante.getNombreCurso());

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(cursoMante, usuarioId));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminar(cursoMante, usuarioId));
        holder.btnRamasTemas.setOnClickListener(v -> listener.onVerRamasYTemas(cursoMante, usuarioId));
        holder.btnHistorialCurso.setOnClickListener(v -> {
            listener.onVerHistorial(cursoMante,usuarioId);
        });
    }
    @Override
    public int getItemCount(){
        return lista.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        //Button btnEditar, btnEliminar, btnRamasTemas;

        ImageButton btnEditar, btnEliminar, btnRamasTemas,btnHistorialCurso;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreCurso);
            btnEditar = itemView.findViewById(R.id.btnEditarCurso);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCurso);
            btnRamasTemas = itemView.findViewById(R.id.btnVerRamasTemas);
            btnHistorialCurso = itemView.findViewById(R.id.btnHistorialCurso);
        }
    }

}
