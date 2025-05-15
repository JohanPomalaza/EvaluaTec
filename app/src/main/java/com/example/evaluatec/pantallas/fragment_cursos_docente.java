package com.example.evaluatec.pantallas;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.Curso;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_cursos_docente extends AppCompatActivity {

    private CursoDocenteAdapter cursoDocenteAdapter;
    private RecyclerView recyclerView;
    private final List<Curso> listaCursos = new ArrayList<>();
    private ApiService apiService;

    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cursos_docente);

        recyclerView = findViewById(R.id.recyclerViewCursosDocente);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cursoDocenteAdapter = new CursoDocenteAdapter(listaCursos, this::onCursoClick);
        recyclerView.setAdapter(cursoDocenteAdapter);

        apiService = ApiCliente.getClient().create(ApiService.class);

        // Obtener el id del usuario
        usuarioId = getIntent().getIntExtra("usuarioId", 0);
        cargarCursos();
    }

    private void cargarCursos() {
        apiService.getCursosPorDocente(usuarioId).enqueue(new Callback<List<Curso>>() {
            @Override
            public void onResponse(@NonNull Call<List<Curso>> call, @NonNull Response<List<Curso>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCursos.clear();
                    listaCursos.addAll(response.body());
                    cursoDocenteAdapter.notifyDataSetChanged();
                } else {
                    mostrarMensajeError("Error al cargar cursos");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Curso>> call, @NonNull Throwable t) {
                mostrarMensajeError("Error de conexión");
            }
        });
    }
    private void onCursoClick(Curso curso) {
        Intent intent = new Intent(this, EstudiantesActivity.class);
        intent.putExtra("cursoId", curso.getIdCurso());
        intent.putExtra("usuarioId", usuarioId);
        startActivity(intent);
    }
    private void mostrarMensajeError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}