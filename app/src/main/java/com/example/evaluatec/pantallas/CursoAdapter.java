package com.example.evaluatec.pantallas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.evaluatec.R;
import com.example.evaluatec.modelos.Curso;
import java.util.List;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CursoViewHolder> {
    private List<Curso> listaCursos;
    private OnCursoClickListener listener;

    public interface OnCursoClickListener {
        void onCursoSelected(Curso curso);
    }
    public CursoAdapter(List<Curso> listaCursos, OnCursoClickListener listener) {
        this.listaCursos = listaCursos;
        this.listener = listener;
    }
    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso_estuadiante, parent, false);
        return new CursoViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
        Curso curso = listaCursos.get(position);
        holder.tvNombreCurso.setText(curso.getNombreCurso());
        holder.itemView.setOnClickListener(v -> listener.onCursoSelected(curso));
    }
    @Override
    public int getItemCount() {
        return listaCursos.size();
    }
    static class CursoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCurso;

        CursoViewHolder(View itemView) {
            super(itemView);
            tvNombreCurso = itemView.findViewById(R.id.tv_nombre_curso);
        }
    }
}
