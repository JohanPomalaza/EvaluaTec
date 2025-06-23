package com.example.evaluatec.adaptadores;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.evaluatec.R;
import com.example.evaluatec.modelos.DocenteDto;
import com.example.evaluatec.modelos.DocenteEditarDto;
import com.example.evaluatec.pantallas.activity_asignaciones;

import java.util.List;

public class ProfesoresAdapter extends RecyclerView.Adapter<ProfesoresAdapter.ViewHolder> {

    public interface OnProfesorActionListener {
        void onEditar(DocenteDto docente);
        void onVerAsignaciones(DocenteDto docente);

    }

    private List<DocenteDto> lista;
    private OnProfesorActionListener listener;
    private int usuarioId;

    public ProfesoresAdapter(List<DocenteDto> lista,int usuarioId, OnProfesorActionListener listener) {
        this.lista = lista;
        this.listener = listener;
        this.usuarioId = usuarioId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profesor, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DocenteDto docente = lista.get(position);
        holder.txtNombre.setText(docente.nombre + " " + docente.apellido);
        holder.txtCorreo.setText(docente.correo);

        holder.imgEditar.setOnClickListener(v -> listener.onEditar(docente));
        holder.itemView.setOnClickListener(v -> listener.onVerAsignaciones(docente));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), activity_asignaciones.class);
            intent.putExtra("idDocente", docente.idUsuario);
            intent.putExtra("nombreDocente", docente.nombre + " " + docente.apellido);
            intent.putExtra("usuarioId", usuarioId);
            holder.itemView.getContext().startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCorreo;
        ImageView imgEditar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreDocente);
            txtCorreo = itemView.findViewById(R.id.txtCorreoDocente);
            imgEditar = itemView.findViewById(R.id.imgEditar);
        }
    }
}
