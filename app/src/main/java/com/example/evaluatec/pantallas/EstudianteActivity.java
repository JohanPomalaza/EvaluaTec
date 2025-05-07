package com.example.evaluatec.pantallas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.api.CursoService;
import com.example.evaluatec.modelos.Curso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EstudianteActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private RecyclerView rvCursos;
    private TextView tvLastAnnouncement;
    private Button btnVerTodosAnuncios;

    private RecyclerView recyclerView;
    private CursoAdapter cursoAdapter;
    private CursoService cursoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_estudiante_activity);

        recyclerView = findViewById(R.id.rvCursos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cursoAdapter = new CursoAdapter(new ArrayList<>());
        recyclerView.setAdapter(cursoAdapter);

        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5262/") // Reemplaza con tu URL base
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cursoService = retrofit.create(CursoService.class);

        // Obtener cursos (asumiendo que ya tienes el token de autenticación)
        obtenerCursos("tu_token_de_autenticacion");


        initViews();
        setupListeners();
    }
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
        tvLastAnnouncement = findViewById(R.id.tvLastAnnouncement);
        btnVerTodosAnuncios = findViewById(R.id.btnVerTodosAnuncios);

        // Configurar RecyclerView
        rvCursos.setLayoutManager(new LinearLayoutManager(this));
        rvCursos.setHasFixedSize(true);
    }

    private void setupListeners() {
        // Botón "Ver todos" los anuncios
        btnVerTodosAnuncios.setOnClickListener(v -> {
            // Navegar a actividad/fragmento con todos los anuncios
            Toast.makeText(this, "Mostrando todos los anuncios", Toast.LENGTH_SHORT).show();
        });

        // Configurar click listeners para las tarjetas de acciones rápidas
        setupQuickActions();
    }
    private void setupQuickActions() {
        // Tareas Pendientes
        CardView cardTareas = findViewById(R.id.card_tareas); // Necesitarías agregar IDs en tu XML
        cardTareas.setOnClickListener(v -> {
            // Navegar a actividad/fragmento de tareas pendientes
            Toast.makeText(this, "Mostrando tareas pendientes", Toast.LENGTH_SHORT).show();
        });

        // Materiales (similar para las otras tarjetas)
        CardView cardMateriales = findViewById(R.id.card_materiales);
        cardMateriales.setOnClickListener(v -> {
            Toast.makeText(this, "Mostrando materiales", Toast.LENGTH_SHORT).show();
        });

        // Calificaciones
        CardView cardCalificaciones = findViewById(R.id.card_calificaciones);
        cardCalificaciones.setOnClickListener(v -> {
            Toast.makeText(this, "Mostrando calificaciones", Toast.LENGTH_SHORT).show();
        });

        // Foro
        CardView cardForo = findViewById(R.id.card_foro);
        cardForo.setOnClickListener(v -> {
            Toast.makeText(this, "Mostrando foro", Toast.LENGTH_SHORT).show();
        });
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
                    .inflate(R.layout.item_curso, parent, false);
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