package com.example.evaluatec.pantallas;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.HistorialNota;
import com.example.evaluatec.modelos.Nota;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotaAdapterDocente extends RecyclerView.Adapter<NotaAdapterDocente.NotaViewHolder> {
    private List<Nota> listaNotas;
    private ApiService apiService;
    private Context context;
    private int alumnoId, usuarioId;

    public NotaAdapterDocente(Context context,List<Nota> listaNotas, int alumnoId, int usuarioId) {
        this.listaNotas = listaNotas;
        this.apiService = ApiCliente.getClient().create(ApiService.class);
        this.context = context;
        this.alumnoId = alumnoId;
        this.usuarioId = usuarioId;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Nota nota = listaNotas.get(position);
        holder.txtTema.setText(nota.getTema());
        holder.edtNota.setText(nota.getNota());
        try {
            double valorNota = Double.parseDouble(nota.getNota());
            if (valorNota < 11) {
                holder.edtNota.setTextColor(Color.RED);
            } else {
                holder.edtNota.setTextColor(Color.BLACK);
            }
        } catch (NumberFormatException e) {
            holder.edtNota.setTextColor(Color.BLACK);
        }
        holder.edtComentario.setText(nota.getComentario());

        holder.btnGuardar.setOnClickListener(v -> {
            String nuevaNota = holder.edtNota.getText().toString().trim();
            String nuevoComentario = holder.edtComentario.getText().toString().trim();

            Log.d("NotaAdapterDocente", "Guardando nota: " + nuevaNota + ", comentario: " + nuevoComentario +
                    ", temaId: " + nota.getIdTema() + ", alumnoId: " + alumnoId + ", docenteId: " + usuarioId);

            if (nuevaNota.isEmpty()) {
                holder.edtNota.setError("La nota no puede estar vacía");
                return;
            }
            try {
                double valorNota = Double.parseDouble(nuevaNota);
                if (valorNota > 20) {
                    holder.edtNota.setError("La nota no puede ser mayor a 20");
                    return;
                }
            } catch (NumberFormatException e) {
                holder.edtNota.setError("Nota inválida");
                return;
            }

            apiService.agregarOEditarNotaConComentario(
                    alumnoId,
                    usuarioId,
                    nota.getIdTema(),
                    nuevaNota,
                    nuevoComentario
            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Nota guardada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al guardar nota", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("NotaAdapterDocente", "Fallo al guardar: " + t.getMessage());
                    Toast.makeText(context, "Fallo de red", Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.btnVerHistorial.setOnClickListener(v -> {
            apiService.obtenerHistorialNota(nota.getIdNota()).enqueue(new Callback<List<HistorialNota>>() {
                @Override
                public void onResponse(Call<List<HistorialNota>> call, Response<List<HistorialNota>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mostrarDialogoHistorial(response.body());
                    } else {
                        Toast.makeText(context, "Error al obtener historial o no existe Historial para este registro.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<HistorialNota>> call, Throwable t) {
                    Toast.makeText(context, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private void mostrarDialogoHistorial(List<HistorialNota> historialNotas) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Historial de cambios");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_historial_nota, null);
        LinearLayout layoutHistorial = view.findViewById(R.id.layoutHistorial);

        if (historialNotas != null && !historialNotas.isEmpty()) {
            for (HistorialNota historial : historialNotas) {
                CardView card = new CardView(context);
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(0, 8, 0, 8);
                card.setLayoutParams(cardParams);
                card.setRadius(16f);
                card.setCardElevation(6f);
                card.setUseCompatPadding(true);

                LinearLayout container = new LinearLayout(context);
                container.setOrientation(LinearLayout.VERTICAL);
                container.setPadding(24, 24, 24, 24);

                TextView tvAccion = crearText("Acción: ", historial.getAccion(), true);
                TextView tvNotas = crearText("Nota anterior: ", String.valueOf(historial.getNotaAnterior()), false);
                TextView tvNotasNueva = crearText("Nota nueva: ", String.valueOf(historial.getNotaNueva()), false);
                TextView tvComentAnterior = crearText("Comentario anterior: ", historial.getComentarioAnterior(), false);
                TextView tvComentNuevo = crearText("Comentario nuevo: ", historial.getComentarioNuevo(), false);
                TextView tvFecha = crearText("Fecha: ", historial.getFechaCambio(), false);
                TextView tvDocente = crearText("Docente: ", historial.getNombreDocente(), false);

                container.addView(tvAccion);
                container.addView(tvNotas);
                container.addView(tvNotasNueva);
                container.addView(tvComentAnterior);
                container.addView(tvComentNuevo);
                container.addView(tvFecha);
                container.addView(tvDocente);

                card.addView(container);
                layoutHistorial.addView(card);
            }
        } else {
            TextView tv = new TextView(context);
            tv.setText("No hay historial para esta nota.");
            tv.setPadding(0, 20, 0, 20);
            tv.setTextSize(16);
            tv.setTextColor(Color.GRAY);
            layoutHistorial.addView(tv);
        }

        builder.setView(view);
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Opcional: estiliza el botón "Cerrar"
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.teal_700));
    }
    private TextView crearText(String label, String value, boolean boldTitle) {
        TextView textView = new TextView(context);
        SpannableString spannable = new SpannableString(label + value);
        if (boldTitle) {
            spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannable);
        textView.setTextSize(15);
        textView.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        textView.setPadding(0, 4, 0, 4);
        return textView;
    }


    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView txtTema;
        EditText edtNota, edtComentario;
        Button btnGuardar,btnVerHistorial;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTema = itemView.findViewById(R.id.txtTema);
            edtNota = itemView.findViewById(R.id.edtNota);
            edtComentario = itemView.findViewById(R.id.edtComentario);
            btnGuardar = itemView.findViewById(R.id.btnGuardarNota);
            btnVerHistorial = itemView.findViewById(R.id.btnHistorial);
        }
    }

}
