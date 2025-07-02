package com.example.evaluatec.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.NotificacionDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.ViewHolder> {

    private final List<NotificacionDto> notificaciones;
    private final ApiService apiService;
    private final Context context;
    private final Runnable onEstadoActualizado;

    public NotificacionAdapter(Context context, List<NotificacionDto> notificaciones, ApiService apiService, Runnable onEstadoActualizado) {
        this.context = context;
        this.notificaciones = notificaciones;
        this.apiService = apiService;
        this.onEstadoActualizado = onEstadoActualizado;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificacionDto noti = notificaciones.get(position);
        holder.tvTitulo.setText(noti.getTitulo());
        holder.tvMensaje.setText(noti.getMensaje());
        holder.tvFecha.setText(noti.getFecha());

        holder.tvTitulo.setTextColor(noti.isLeida() ? Color.GRAY : Color.BLACK);

        // Opcional: cambiar color si está leída
        holder.itemView.setOnClickListener(v -> {
            if (!noti.isLeida()) {
                apiService.marcarComoLeida(noti.getIdNotificacion()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            noti.setLeida(true);
                            notifyItemChanged(holder.getAdapterPosition());
                            if (onEstadoActualizado != null) onEstadoActualizado.run();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(context, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvMensaje, tvFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvMensaje = itemView.findViewById(R.id.tvMensaje);
            tvFecha = itemView.findViewById(R.id.tvFecha);
        }
    }
}
