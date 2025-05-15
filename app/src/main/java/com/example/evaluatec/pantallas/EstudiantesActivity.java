package com.example.evaluatec.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.Estudiante;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstudiantesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EstudianteAdapter estudianteAdapter;
    private List<Estudiante> listaEstudiantes = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes);

        recyclerView = findViewById(R.id.recyclerViewEstudiantes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        estudianteAdapter = new EstudianteAdapter(listaEstudiantes, this::irANotas);
        recyclerView.setAdapter(estudianteAdapter);

        apiService = ApiCliente.getClient().create(ApiService.class);

        int usuarioId = getIntent().getIntExtra("usuarioId", 0);
        int cursoId = getIntent().getIntExtra("cursoId", 0);

        cargarEstudiantes(usuarioId, cursoId);
    }

    private void cargarEstudiantes(int usuarioId, int cursoId) {
        apiService.getEstudiantesPorCurso(usuarioId, cursoId).enqueue(new Callback<List<Estudiante>>() {
            @Override
            public void onResponse(Call<List<Estudiante>> call, Response<List<Estudiante>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaEstudiantes.clear();
                    listaEstudiantes.addAll(response.body());
                    estudianteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Estudiante>> call, Throwable t) {
                Toast.makeText(EstudiantesActivity.this, "Error al cargar estudiantes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void irANotas(Estudiante estudiante) {
        Intent intent = new Intent(this, TemasNotasActivity.class);
        intent.putExtra("usuarioId", getIntent().getIntExtra("usuarioId", 0));
        intent.putExtra("cursoId", getIntent().getIntExtra("cursoId", 0));
        intent.putExtra("estudianteId", estudiante.getIdUsuarioEstudiante());
        startActivity(intent);
    }
}