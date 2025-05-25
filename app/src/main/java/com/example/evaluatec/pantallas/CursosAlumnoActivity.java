package com.example.evaluatec.pantallas;

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
import com.example.evaluatec.modelos.Curso;
import com.example.evaluatec.modelos.Estudiante;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursosAlumnoActivity extends AppCompatActivity {

    private ApiService apiService;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cursos_alumno_activiy);

        recyclerView = findViewById(R.id.recyclerViewCursosAlumno);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        apiService = ApiCliente.getClient().create(ApiService.class);


        int alumnoId = getIntent().getIntExtra("alumnoId", 0);
        int anioEscolar = getIntent().getIntExtra("anioEscolar", 0);

        apiService.getCursosPorAlumno(alumnoId).enqueue(new Callback<List<Curso>>() {
            @Override
            public void onResponse(Call<List<Curso>> call, Response<List<Curso>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Curso> cursosOriginales = response.body();
                    Set<String> nombresCursos = new HashSet<>();
                    List<Curso> cursosUnicos = new ArrayList<>();

                    for (Curso curso : cursosOriginales) {
                        if (!nombresCursos.contains(curso.getNombreCurso())) {
                            nombresCursos.add(curso.getNombreCurso());
                            cursosUnicos.add(curso);
                        }
                    }
                    CursoAlumnoAdapter cursoAlumnoAdapter = new CursoAlumnoAdapter(cursosUnicos, alumnoId, anioEscolar);
                    recyclerView.setAdapter(cursoAlumnoAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Curso>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al cargar cursos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}