package com.example.evaluatec.pantallas;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.adaptadores.cursoMantenimientoAdapter;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.CursoMantenimiento;
import com.example.evaluatec.modelos.CursoUpdateDTO;
import com.example.evaluatec.modelos.HistorialCurso;
import com.example.evaluatec.modelos.HistorialNota;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursoMantenimientoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private cursoMantenimientoAdapter adapter;
    private List<CursoMantenimiento> cursos = new ArrayList<>();
    private Button btnAgregar;
    private ApiService apiService;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_curso_mantenimiento);

        recyclerView  = findViewById(R.id.recyclerCursos);
        btnAgregar = findViewById(R.id.btnAgregarCurso);
        apiService = ApiCliente.getClient().create(ApiService.class);

        usuarioId = getIntent().getIntExtra("usuarioId", 0);

        adapter = new cursoMantenimientoAdapter(cursos, new cursoMantenimientoAdapter.OnCursoListener() {
            @Override
            public void onEditar(CursoMantenimiento cursoMante, int usuarioId) {
                mostrarDialogoCurso(cursoMante, usuarioId);
            }

            @Override
            public void onEliminar(CursoMantenimiento cursoMante, int usuarioId) {
                eliminarCurso(cursoMante, usuarioId);
            }
            @Override
            public void onVerHistorial(CursoMantenimiento cursoMante, int usuarioId) {
                verHistorialCurso(cursoMante, usuarioId);
            }

            @Override
            public void onVerRamasYTemas(CursoMantenimiento cursoMante, int usuarioId) {
                Intent intent = new Intent(CursoMantenimientoActivity.this, RamasTemasActivity.class);
                intent.putExtra("cursoId", cursoMante.getIdCurso());
                intent.putExtra("cursoNombre", cursoMante.getNombreCurso());
                intent.putExtra("usuarioId", usuarioId);

                startActivity(intent);
            }
        }, usuarioId);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> mostrarDialogoCurso(null, usuarioId));

        cargarCursos();
    }

    private void cargarCursos() {
        apiService.getCursos().enqueue(new Callback<List<CursoMantenimiento>>() {
            @Override
            public void onResponse(Call<List<CursoMantenimiento>> call, Response<List<CursoMantenimiento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cursos.clear();
                    cursos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<CursoMantenimiento>> call, Throwable t) {
                Toast.makeText(CursoMantenimientoActivity.this, "Error al cargar cursos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoCurso(@Nullable CursoMantenimiento cursoEditar, int usuarioId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(cursoEditar == null ? "Nuevo Curso" : "Editar Curso");

        View view = getLayoutInflater().inflate(R.layout.dialog_curso, null);
        EditText edtNombre = view.findViewById(R.id.edtNombreCurso);

        if (cursoEditar != null) {
            edtNombre.setText(cursoEditar.getNombreCurso());
        }

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = edtNombre.getText().toString().trim();

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            CursoMantenimiento curso = new CursoMantenimiento();
            curso.setNombreCurso(nombre);

            if (cursoEditar == null) {
                crearCurso(curso, usuarioId);
            } else {
                curso.setIdCurso(cursoEditar.getIdCurso());
                editarCurso(curso, usuarioId);
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void crearCurso(CursoMantenimiento cursoMante, int usuarioId) {
        apiService.crearCurso(cursoMante, usuarioId).enqueue(new Callback<CursoMantenimiento>() {
            @Override
            public void onResponse(Call<CursoMantenimiento> call, Response<CursoMantenimiento> response) {
                Toast.makeText(CursoMantenimientoActivity.this, "Se creo el curso correctamente", Toast.LENGTH_SHORT).show();
                cargarCursos();
            }

            @Override
            public void onFailure(Call<CursoMantenimiento> call, Throwable t) {
                Toast.makeText(CursoMantenimientoActivity.this, "Error al crear", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editarCurso(CursoMantenimiento cursoMante, int usuarioId) {
        if (cursoMante.getIdCurso() == 0) {
            Toast.makeText(this, "ID del curso inválido", Toast.LENGTH_SHORT).show();
            return;
        }
        CursoUpdateDTO dto = new CursoUpdateDTO(cursoMante.getNombreCurso(), usuarioId);

        apiService.editarCurso(cursoMante.getIdCurso(), dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CursoMantenimientoActivity.this, "Curso actualizado", Toast.LENGTH_SHORT).show();
                    cargarCursos(); // refresca la lista
                } else {
                    Toast.makeText(CursoMantenimientoActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CursoMantenimientoActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarCurso(CursoMantenimiento cursoMante, int usuarioId) {
        apiService.eliminarCurso(cursoMante.getIdCurso(),usuarioId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CursoMantenimientoActivity.this, "Curso eliminado", Toast.LENGTH_SHORT).show();
                    cargarCursos();
                } else {
                    Toast.makeText(CursoMantenimientoActivity.this, "Error al eliminar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CursoMantenimientoActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void verHistorialCurso(CursoMantenimiento cursoMante, int usuarioId) {
        apiService.obtenerHistorialCurso(cursoMante.getIdCurso()).enqueue(new Callback<List<HistorialCurso>>() {
            @Override
            public void onResponse(Call<List<HistorialCurso>> call, Response<List<HistorialCurso>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<HistorialCurso> historial = response.body();
                    mostrarDialogoHistorial(historial);
                } else {
                    Toast.makeText(CursoMantenimientoActivity.this, "No se pudo obtener el historial", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HistorialCurso>> call, Throwable t) {
                Toast.makeText(CursoMantenimientoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoHistorial(List<HistorialCurso> historialCurso) {
        Context context = this; // Usa el contexto de la activity
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Historial del curso");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_historial_curso, null);
        LinearLayout layoutCurso = view.findViewById(R.id.layoutCurso);

        if (historialCurso != null && !historialCurso.isEmpty()) {
            for (HistorialCurso historial : historialCurso) {
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
                TextView tvNombreAnterior = crearText("Nombre anterior: ", historial.getNombreAnterior(), false);
                TextView tvNombreNuevo = crearText("Nombre nuevo: ", historial.getNombreNuevo(), false);
                TextView tvFecha = crearText("Fecha: ", historial.getFechaCambio(), false);
                TextView tvUsuario = crearText("Usuario responsable: ", historial.getNombreUsuario(), false);

                container.addView(tvAccion);
                container.addView(tvNombreAnterior);
                container.addView(tvNombreNuevo);
                container.addView(tvFecha);
                container.addView(tvUsuario);

                card.addView(container);
                layoutCurso.addView(card);
            }
        } else {
            TextView tv = new TextView(context);
            tv.setText("No hay historial para este curso.");
            tv.setPadding(0, 20, 0, 20);
            tv.setTextSize(16);
            tv.setTextColor(Color.GRAY);
            layoutCurso.addView(tv);
        }

        builder.setView(view);
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.teal_700));
    }
    private TextView crearText(String label, String value, boolean boldTitle) {
        Context context = this;
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
}