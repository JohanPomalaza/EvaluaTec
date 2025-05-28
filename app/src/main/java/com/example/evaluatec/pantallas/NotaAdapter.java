package com.example.evaluatec.pantallas;

import android.content.Context;
import android.graphics.Color;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.modelos.Nota;
import com.example.evaluatec.modelos.NotaPorCurso;
import com.google.android.material.chip.Chip;

import java.util.List;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {
    private List<NotaPorCurso> listaNotas;
    private Context context;

    public NotaAdapter(List<NotaPorCurso> listaNotas, Context context) {
        this.listaNotas = listaNotas;
        this.context = context;
    }
    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso_detalle, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        NotaPorCurso nota = listaNotas.get(position);

        holder.tvNombreTema.setText("" + (nota.getTema() != null ? nota.getTema() : "Sin Tema"));
        holder.tvNota.setText("" + String.valueOf(nota.getNota()));
        holder.tvNombreRama.setText("" + (nota.getRama() != null ? nota.getRama() : "Sin Rama"));
        try {
            double valorNota = Double.parseDouble(nota.getNota());
            if (valorNota < 11) {
                holder.chipEstado.setText("Desaprobado");
                holder.chipEstado.setChipBackgroundColorResource(R.color.red_light);
                holder.chipEstado.setTextColor(ContextCompat.getColor(context, R.color.red_dark));
            } else {
                holder.chipEstado.setText("Aprobado");
                holder.chipEstado.setChipBackgroundColorResource(R.color.green_light);
                holder.chipEstado.setTextColor(ContextCompat.getColor(context, R.color.green_dark));
            }
            int color = valorNota < 11 ?
                    ContextCompat.getColor(context, R.color.red_dark) :
                    ContextCompat.getColor(context, R.color.primary);
            holder.tvNota.setTextColor(color);
            holder.progressBar.setProgress((int) valorNota * 10);
        } catch (NumberFormatException e) {
            holder.tvNota.setTextColor(Color.BLACK);
        }
        if (nota.getComentario() != null && !nota.getComentario().isEmpty()) {
            holder.tvComentario.setText(nota.getComentario());
        } else {
            holder.tvComentario.setText("Sin comentarios");
        }
        // Configurar el acordeón
        holder.btnToggleComentario.setOnClickListener(v -> {
            boolean isExpanded = holder.containerComentario.getVisibility() == View.VISIBLE;
            toggleAcordeon(holder, !isExpanded);
        });

        // Estado inicial (cerrado)
        holder.containerComentario.setVisibility(View.GONE);
        holder.ivArrow.setRotation(0f);
    }
    private void toggleAcordeon(NotaViewHolder holder, boolean shouldExpand) {
        TransitionManager.beginDelayedTransition(holder.itemView.findViewById(R.id.cardView));

        if (shouldExpand) {
            holder.containerComentario.setVisibility(View.VISIBLE);
            holder.ivArrow.animate().rotation(180f).setDuration(200).start();
        } else {
            holder.containerComentario.setVisibility(View.GONE);
            holder.ivArrow.animate().rotation(0f).setDuration(200).start();
        }
    }
    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public void actualizarLista(List<NotaPorCurso> nuevasNotas) {
        this.listaNotas = nuevasNotas;
        notifyDataSetChanged();
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreTema, tvNota, tvNombreRama, tvComentario;
        LinearLayout btnToggleComentario, containerComentario;
        ImageView ivArrow;
        Chip chipEstado;
        ProgressBar progressBar;

        NotaViewHolder(View itemView) {
            super(itemView);
            tvNombreTema = itemView.findViewById(R.id.tvNombreTema);
            tvNota = itemView.findViewById(R.id.tvNota);
            tvNombreRama = itemView.findViewById(R.id.tvNombreRama);
            tvComentario = itemView.findViewById(R.id.tvComentario);
            btnToggleComentario = itemView.findViewById(R.id.btnToggleComentario);
            containerComentario = itemView.findViewById(R.id.containerComentario);
            ivArrow = itemView.findViewById(R.id.ivArrow);
            chipEstado = itemView.findViewById(R.id.chipEstado);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
