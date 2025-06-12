package com.example.evaluatec.pantallas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.Curso;

import java.util.List;


public class CursoAlumnoAdapter extends RecyclerView.Adapter<CursoAlumnoAdapter.ViewHolder> {
    private List<Curso> listaCursos;
    private int alumnoId, anioEscolar, usuarioId;
    private ApiService apiService;

    public CursoAlumnoAdapter(List<Curso> listaCursos, int alumnoId, int anioEscolar, int usuarioId) {
        this.listaCursos = listaCursos;
        this.alumnoId = alumnoId;
        this.anioEscolar = anioEscolar;
        this.usuarioId = usuarioId;
        this.apiService = ApiCliente.getClient().create(ApiService.class);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso, parent, false);
        return new ViewHolder(vista);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Curso curso = listaCursos.get(position);
        holder.txtNombreCurso.setText(curso.getNombreCurso());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TemasNotasActivity.class);
            intent.putExtra("alumnoId", alumnoId);
            intent.putExtra("idRama", curso.getIdRama());
            intent.putExtra("anioEscolar", anioEscolar);
            intent.putExtra("usuarioId", usuarioId);
            v.getContext().startActivity(intent);
        });
        }
        @Override
        public int getItemCount() {
            return listaCursos.size();
        }
   public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreCurso;
        public  ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreCurso = itemView.findViewById(R.id.txtNombreCurso);
        }
   }
}
