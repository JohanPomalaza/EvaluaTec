package com.example.evaluatec.pantallas;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.adaptadores.cursoMantenimientoAdapter;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.CursoMantenimiento;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_curso_mantenimiento);

        recyclerView  = findViewById(R.id.recyclerCursos);
        btnAgregar = findViewById(R.id.btnAgregarCurso);
        apiService = ApiCliente.getClient().create(ApiService.class);

        adapter = new cursoMantenimientoAdapter(cursos, new cursoMantenimientoAdapter.OnCursoListener() {
            @Override
            public void onEditar(CursoMantenimiento cursoMante) {
                mostrarDialogoCurso(cursoMante);
            }

            @Override
            public void onEliminar(CursoMantenimiento cursoMante) {
                eliminarCurso(cursoMante.getIdCurso());
            }

            @Override
            public void onVerRamasYTemas(CursoMantenimiento cursoMante) {
                Intent intent = new Intent(CursoMantenimientoActivity.this, RamasTemasActivity.class);
                intent.putExtra("cursoId", cursoMante.getIdCurso());
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> mostrarDialogoCurso(null));

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

    private void mostrarDialogoCurso(@Nullable CursoMantenimiento cursoEditar) {
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
                crearCurso(curso);
            } else {
                // ✅ Preservar el ID para que PUT funcione correctamente
                curso.setIdCurso(cursoEditar.getIdCurso());
                editarCurso(curso);
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void crearCurso(CursoMantenimiento cursoMante) {
        apiService.crearCurso(cursoMante).enqueue(new Callback<CursoMantenimiento>() {
            @Override
            public void onResponse(Call<CursoMantenimiento> call, Response<CursoMantenimiento> response) {
                cargarCursos();
            }

            @Override
            public void onFailure(Call<CursoMantenimiento> call, Throwable t) {
                Toast.makeText(CursoMantenimientoActivity.this, "Error al crear", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editarCurso(CursoMantenimiento cursoMante) {
        if (cursoMante.getIdCurso() == 0) {
            Toast.makeText(this, "ID del curso inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.editarCurso(cursoMante.getIdCurso(), cursoMante).enqueue(new Callback<Void>() {
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

    private void eliminarCurso(int idCurso) {
        apiService.eliminarCurso(idCurso).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                cargarCursos();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CursoMantenimientoActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}