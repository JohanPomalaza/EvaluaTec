package com.example.evaluatec.pantallas;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
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
    private int alumnoId;

    public NotaAdapterDocente(Context context,List<Nota> listaNotas, int alumnoId) {
        this.listaNotas = listaNotas;
        this.apiService = ApiCliente.getClient().create(ApiService.class);
        this.context = context;
        this.alumnoId = alumnoId;
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
                    ", temaId: " + nota.getIdTema() + ", alumnoId: " + alumnoId);

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
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView txtTema;
        EditText edtNota, edtComentario;
        Button btnGuardar;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTema = itemView.findViewById(R.id.txtTema);
            edtNota = itemView.findViewById(R.id.edtNota);
            edtComentario = itemView.findViewById(R.id.edtComentario);
            btnGuardar = itemView.findViewById(R.id.btnGuardarNota);
        }
    }

}
