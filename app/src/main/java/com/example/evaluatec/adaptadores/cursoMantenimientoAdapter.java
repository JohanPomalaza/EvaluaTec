package com.example.evaluatec.adaptadores;

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

    public interface OnCursoListener {
        void onEditar(CursoMantenimiento cursoMante);
        void onEliminar(CursoMantenimiento cursoMante);
        void onVerRamasYTemas(CursoMantenimiento cursoMante);
    }

    public cursoMantenimientoAdapter(List<CursoMantenimiento> lista, OnCursoListener listener) {
        this.lista = lista;
        this.listener = listener;
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

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(cursoMante));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminar(cursoMante));
        holder.btnRamasTemas.setOnClickListener(v -> listener.onVerRamasYTemas(cursoMante));
    }
    @Override
    public int getItemCount(){
        return lista.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        //Button btnEditar, btnEliminar, btnRamasTemas;

        ImageButton btnEditar, btnEliminar, btnRamasTemas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreCurso);
            btnEditar = itemView.findViewById(R.id.btnEditarCurso);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCurso);
            btnRamasTemas = itemView.findViewById(R.id.btnVerRamasTemas);
        }
    }

}
