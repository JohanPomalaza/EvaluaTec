package com.example.evaluatec.pantallas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import com.example.evaluatec.R;
import com.example.evaluatec.api.CursoService;
import com.example.evaluatec.modelos.Curso;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EstudianteActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private RecyclerView rvCursos;

    private RecyclerView recyclerView;
    private CursoAdapter cursoAdapter;
    private CursoService cursoService;

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_estudiante_activity);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        recyclerView = findViewById(R.id.rvCursos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cursoAdapter = new CursoAdapter(new ArrayList<>());
        recyclerView.setAdapter(cursoAdapter);
        String baseUrl = "http://10.0.2.2:5262/";
        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) // Reemplaza con tu URL base
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cursoService = retrofit.create(CursoService.class);

        // Obtener cursos (asumiendo que ya tienes el token de autenticación)
        obtenerCursos("tu_token_de_autenticacion");


        initViews();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Handle home selection
                return true;
            } else if (id == R.id.nav_search) {
                // Handle search selection
                return true;
            } else if (id == R.id.nav_porelegir) {
                // Handle porelegir selection
                return true;
            } else if (id == R.id.nav_reportes) {
                // Handle reportes selection
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(EstudianteActivity.this, perfil_activity.class));
                return true;
            }
            return false;
        }
    };


    private void obtenerCursos(String authToken) {
        Call<List<Curso>> call = cursoService.getCursos("Bearer " + authToken);

        call.enqueue(new Callback<List<Curso>>() {
            @Override
            public void onResponse(Call<List<Curso>> call, Response<List<Curso>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Curso> cursos = response.body();
                    // Actualizar el adaptador con los nuevos datos
                    cursoAdapter.actualizarLista(cursos);
                } else {
                    Toast.makeText(EstudianteActivity.this, "Error al obtener cursos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Curso>> call, Throwable t) {
                Toast.makeText(EstudianteActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
}
    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        rvCursos = findViewById(R.id.rvCursos);

        // Configurar RecyclerView
        rvCursos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCursos.setHasFixedSize(true);
    }

    // Adaptador para RecyclerView
    private static class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CursoViewHolder> {
        private List<Curso> cursos;

        public CursoAdapter(List<Curso> cursos) {
            this.cursos = cursos;
        }

        public void actualizarLista(List<Curso> nuevosCursos) {
            this.cursos = nuevosCursos;
            notifyDataSetChanged(); // Notifica al RecyclerView que los datos han cambiado
        }
        @NonNull
        @Override
        public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_curso_estuadiante, parent, false);
            return new CursoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
            Curso curso = cursos.get(position);
            holder.bind(curso);
        }

        @Override
        public int getItemCount() {
            return cursos.size();
        }

        static class CursoViewHolder extends RecyclerView.ViewHolder {
            private TextView tvNombreCurso;

            public CursoViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNombreCurso = itemView.findViewById(R.id.tv_nombre_curso);
            }
            public void bind(Curso curso) {
                tvNombreCurso.setText(curso.getNombre_curso());

                // Opcional: Click listener para cada item
                itemView.setOnClickListener(v -> {
                    // Puedes abrir una nueva actividad o mostrar detalles
                    Toast.makeText(itemView.getContext(),
                            "Curso seleccionado: " + curso.getNombre_curso(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        }
    }
}