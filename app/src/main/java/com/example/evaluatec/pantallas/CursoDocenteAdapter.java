package com.example.evaluatec.pantallas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.modelos.Curso;

import java.util.List;

public class CursoDocenteAdapter extends RecyclerView.Adapter<CursoDocenteAdapter.CursoDocenteViewHolder> {
    private List<Curso> listaCursos;
    private final CursoDocenteAdapter.OnCursoClickListener listener;

    public CursoDocenteAdapter(List<Curso> listaCursos, CursoDocenteAdapter.OnCursoClickListener listener) {
        this.listaCursos = listaCursos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CursoDocenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso, parent, false);
        return new CursoDocenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CursoDocenteViewHolder holder, int position) {
        Curso curso = listaCursos.get(position);
        if (curso != null && curso.getNombreCurso() != null) {
            holder.tvNombreCurso.setText(curso.getNombreCurso());
            holder.itemView.setOnClickListener(v -> listener.onCursoClick(curso));
        } else {
            holder.tvNombreCurso.setText("Curso desconocido");
        }
    }
    public interface OnCursoClickListener {
        void onCursoClick(Curso curso);
    }

    @Override
    public int getItemCount() {
        return listaCursos.size();
    }

    static class CursoDocenteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCurso;

        CursoDocenteViewHolder(View itemView) {
            super(itemView);
            tvNombreCurso = itemView.findViewById(R.id.tv_nombre_curso);
        }
    }
}
