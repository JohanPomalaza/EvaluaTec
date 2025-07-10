package com.example.evaluatec.pantallas;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.HistorialNota;
import com.example.evaluatec.modelos.Nota;
import com.example.evaluatec.modelos.NotaEstudiante;
import com.example.evaluatec.modelos.RamaCursoCrearDTO;
import com.example.evaluatec.modelos.TemaDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemasNotasActivity extends AppCompatActivity {

    private RecyclerView recyclerTemas;
    private NotaAdapterDocente temasAdapter;
    private int idRama, alumnoId, anioEscolar, usuarioId;
    private ApiService apiService;
    private FloatingActionButton btnAgregarNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas_notas);

        recyclerTemas = findViewById(R.id.recyclerViewNotas);
        recyclerTemas.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiCliente.getClient().create(ApiService.class);

        idRama = getIntent().getIntExtra("idRama", -1);
        alumnoId = getIntent().getIntExtra("alumnoId", -1);
        anioEscolar = getIntent().getIntExtra("anioEscolar", -1);
        usuarioId = getIntent().getIntExtra("usuarioId", -1);

        btnAgregarNota = findViewById(R.id.btnAgregarNota);


        cargarTemas();

        btnAgregarNota.setOnClickListener(v -> {
            mostrarDialogoCrearNota(alumnoId, idRama, usuarioId);
        });


    }
    private void cargarTemas() {
        apiService.getNotasPorCurso(alumnoId, idRama, anioEscolar).enqueue(new Callback<List<Nota>>() {
            @Override
            public void onResponse(Call<List<Nota>> call, Response<List<Nota>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Nota> notas = response.body();
                    temasAdapter = new NotaAdapterDocente(TemasNotasActivity.this, response.body(), alumnoId, usuarioId, notaCallback);
                    recyclerTemas.setAdapter(temasAdapter);

                } else {
                    Toast.makeText(TemasNotasActivity.this, "No hay temas para este curso", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Nota>> call, Throwable t) {
                Toast.makeText(TemasNotasActivity.this, "Error al cargar temas: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final NotaAdapterDocente.NotaCallback notaCallback = new NotaAdapterDocente.NotaCallback() {
        @Override
        public void onGuardarNota(Nota nota, String nuevaNota, String nuevoComentario, String justificacion) {
            try {
                double valorNota = Double.parseDouble(nuevaNota);
                if (valorNota < 0 || valorNota > 20) {
                    Toast.makeText(TemasNotasActivity.this, "La nota debe estar entre 0 y 20", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(TemasNotasActivity.this, "Formato de nota inválido", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("NotaDebug", "Guardando nota: est=" + alumnoId + ", doc=" + usuarioId +
                    ", tema=" + nota.getIdTema() + ", nota=" + nuevaNota + ", comentario =" + nuevoComentario + ", justi =" + justificacion);
            apiService.agregarOEditarNotaConComentario(
                    alumnoId,nota.getIdTema(),nuevaNota,nuevoComentario,usuarioId,justificacion
            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(TemasNotasActivity.this, "Nota guardada", Toast.LENGTH_SHORT).show();
                        cargarTemas();
                    } else {
                        Toast.makeText(TemasNotasActivity.this, "Error al guardar nota", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(TemasNotasActivity.this, "Fallo de red", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onVerHistorial(int idNota) {
            apiService.obtenerHistorialNota(idNota).enqueue(new Callback<List<HistorialNota>>() {
                @Override
                public void onResponse(Call<List<HistorialNota>> call, Response<List<HistorialNota>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mostrarDialogoHistorial(response.body());
                    } else {
                        Toast.makeText(TemasNotasActivity.this, "No hay historial para esta nota", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<HistorialNota>> call, Throwable t) {
                    Toast.makeText(TemasNotasActivity.this, "Error de red al obtener historial", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private void mostrarDialogoHistorial(List<HistorialNota> historialNotas) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Historial de cambios");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_historial_nota, null);
        LinearLayout layoutHistorial = view.findViewById(R.id.layoutHistorial);

        if (historialNotas != null && !historialNotas.isEmpty()) {
            for (HistorialNota historial : historialNotas) {
                View cardView = LayoutInflater.from(this).inflate(R.layout.item_historial_card, layoutHistorial, false);

                TextView tvAccion = cardView.findViewById(R.id.tvAccion);
                TextView tvNotas = cardView.findViewById(R.id.tvNotaAnterior);
                TextView tvNotasNueva = cardView.findViewById(R.id.tvNotaNueva);
                TextView tvComentAnterior = cardView.findViewById(R.id.tvComentarioAnterior);
                TextView tvComentNuevo = cardView.findViewById(R.id.tvComentarioNuevo);
                TextView tvFecha = cardView.findViewById(R.id.tvFecha);
                TextView tvDocente = cardView.findViewById(R.id.tvDocente);
                TextView tvJustificacion = cardView.findViewById(R.id.tvJustificacion);

                tvAccion.setText("Acción: " + historial.getAccion());
                tvNotas.setText("Nota anterior: " + historial.getNotaAnterior());
                tvNotasNueva.setText("Nota nueva: " + historial.getNotaNueva());
                tvComentAnterior.setText("Comentario anterior: " + historial.getComentarioAnterior());
                tvComentNuevo.setText("Comentario nuevo: " + historial.getComentarioNuevo());
                tvFecha.setText("Fecha: " + historial.getFechaCambio());
                tvDocente.setText("Docente: " + historial.getNombreDocente());
                tvJustificacion.setText("Justificación: " + historial.getJustificacion());

                layoutHistorial.addView(cardView);
            }
        } else {
            TextView tv = new TextView(this);
            tv.setText("No hay historial disponible.");
            tv.setPadding(0, 20, 0, 20);
            tv.setTextSize(16);
            layoutHistorial.addView(tv);
        }

        builder.setView(view);
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void mostrarDialogoCrearNota(int idEstudiante, int idCurso, int idDocente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_nueva_nota, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        AutoCompleteTextView autoCompleteTema = view.findViewById(R.id.autoCompleteTema);
        TextInputEditText editTextNota = view.findViewById(R.id.editTextNota);
        TextInputEditText editTextComentario = view.findViewById(R.id.editTextComentario);
        TextInputEditText editTextJustificacion = view.findViewById(R.id.editTextJustificacion);
        Button btnGuardar = view.findViewById(R.id.btnGuardarNota);
        List<TemaDTO> listaTemas = new ArrayList<>();

        apiService.getTemasPendientes(idEstudiante, idCurso).enqueue(new Callback<List<TemaDTO>>() {
            @Override
            public void onResponse(Call<List<TemaDTO>> call, Response<List<TemaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaTemas.clear();
                    listaTemas.addAll(response.body());
                    List<String> nombresTemas = new ArrayList<>();
                    for (TemaDTO tema : listaTemas) {
                        nombresTemas.add(tema.getNombre());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TemasNotasActivity.this,
                            android.R.layout.simple_dropdown_item_1line, nombresTemas);
                    autoCompleteTema.setAdapter(adapter);
                } else {
                    Toast.makeText(TemasNotasActivity.this, "No se pudieron cargar los temas.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<TemaDTO>> call, Throwable t) {
                Toast.makeText(TemasNotasActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnGuardar.setOnClickListener(v -> {
            String nombreTema = autoCompleteTema.getText().toString().trim();
            String notaStr = editTextNota.getText().toString().trim();
            String comentario = editTextComentario.getText().toString().trim();
            String justificacion = editTextJustificacion.getText().toString().trim();

            if (nombreTema.isEmpty() || notaStr.isEmpty()) {
                Toast.makeText(TemasNotasActivity.this, "Debe completar todos los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            double valorNota;
            try {
                valorNota = Double.parseDouble(notaStr);
                if (valorNota < 0 || valorNota > 20) {
                    Toast.makeText(TemasNotasActivity.this, "La nota debe estar entre 0 y 20", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(TemasNotasActivity.this, "Formato de nota inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            TemaDTO temaSeleccionado = null;
            for (TemaDTO tema : listaTemas) {
                if (tema.getNombre().equals(nombreTema)) {
                    temaSeleccionado = tema;
                    break;
                }
            }

            if (temaSeleccionado == null) {
                Toast.makeText(TemasNotasActivity.this, "Tema seleccionado no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            for (TemaDTO t : listaTemas) {
                if (t.getOrden() < temaSeleccionado.getOrden()) {
                    Toast.makeText(TemasNotasActivity.this, "Debe registrar primero la nota del tema anterior: " + t.getNombre(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Log.d("NotaDebug", "Guardando nota: est=" + idEstudiante + ", doc=" + idDocente +
                    ", tema=" + temaSeleccionado.getIdTema() + ", nota=" + notaStr + ", comentario=" + comentario + ", justi=" + justificacion);

            apiService.agregarOEditarNotaConComentario(
                    idEstudiante,
                    temaSeleccionado.getIdTema(),
                    notaStr,
                    comentario,
                    idDocente,
                    justificacion
            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(TemasNotasActivity.this, "Nota registrada correctamente", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        cargarTemas();
                    } else {
                        Toast.makeText(TemasNotasActivity.this, "Error al registrar nota", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(TemasNotasActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}