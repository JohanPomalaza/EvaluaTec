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

    public interface NotaCallback {
        void onGuardarNota(Nota nota, String nuevaNota, String nuevoComentario, String justificacion);
        void onVerHistorial(int idNota);
    }

    private List<Nota> listaNotas;
    private Context context;
    private NotaCallback callback;

    public NotaAdapterDocente(Context context, List<Nota> listaNotas,int alumnoId, int usuarioId, NotaCallback callback) {
        this.listaNotas = listaNotas;
        this.context = context;
        this.callback = callback;
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
        holder.edtComentario.setText(nota.getComentario());
        holder.edtJustificacion.setText(nota.getJustificacion());

        holder.btnGuardar.setOnClickListener(v -> {
            String nuevaNota = holder.edtNota.getText().toString().trim();
            String nuevoComentario = holder.edtComentario.getText().toString().trim();
            String justificacion = holder.edtJustificacion.getText().toString().trim();

            if (nuevaNota.isEmpty()) {
                holder.edtNota.setError("La nota no puede estar vacía");
                return;
            }
            if (justificacion.isEmpty()) {
                holder.edtJustificacion.setError("Justificación obligatoria");
                return;
            }

            callback.onGuardarNota(nota, nuevaNota, nuevoComentario, justificacion);
        });

        holder.btnVerHistorial.setOnClickListener(v -> callback.onVerHistorial(nota.getIdNota()));
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView txtTema;
        EditText edtNota, edtComentario, edtJustificacion;
        Button btnGuardar, btnVerHistorial;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTema = itemView.findViewById(R.id.txtTema);
            edtNota = itemView.findViewById(R.id.edtNota);
            edtComentario = itemView.findViewById(R.id.edtComentario);
            edtJustificacion = itemView.findViewById(R.id.edtJustificacion);
            btnGuardar = itemView.findViewById(R.id.btnEditarNota);
            btnVerHistorial = itemView.findViewById(R.id.btnHistorial);
        }
    }
}
