package com.example.evaluatec.pantallas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.google.android.material.navigation.NavigationBarView;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.Curso;
import com.example.evaluatec.modelos.Nota;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    public class EstudianteActivity extends AppCompatActivity {

        private TextView tvWelcome;
        private BottomNavigationView bottomNav;
        private RecyclerView rvCursos, rvDetalleCursos;
        private CursoAdapter cursoAdapter;
        private NotaAdapter notaAdapter;
        private List<Curso> listaCursos = new ArrayList<>();
        private List<Nota> listaNotas = new ArrayList<>();
        private ApiService apiService;
        private int usuarioId;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_estudiante_activity);

            initViews();
            setupRecyclerViews();
            setupApiService();
            obtenerDatosUsuario();
            cargarCursos();
            setupBottomNavigation();
        }

        private void initViews() {
            tvWelcome = findViewById(R.id.tvWelcome);
            bottomNav = findViewById(R.id.bottom_navigation);
            rvCursos = findViewById(R.id.rvCursos);
            rvDetalleCursos = findViewById(R.id.rvDetalleCursos);
        }

        private void setupBottomNavigation() {
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    // Handle home selection
                    return true;
                } else if (itemId == R.id.nav_search) {
                    // Handle search selection
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(EstudianteActivity.this, perfil_activity.class));
                    return true;
                }
                return false;
            });
        }

        private void setupRecyclerViews() {
            // Configurar RecyclerView de Cursos
            cursoAdapter = new CursoAdapter(listaCursos, this::onCursoSelected);
            rvCursos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvCursos.setAdapter(cursoAdapter);

            // Configurar RecyclerView de Notas
            notaAdapter = new NotaAdapter(listaNotas);
            rvDetalleCursos.setLayoutManager(new LinearLayoutManager(this));
            rvDetalleCursos.setAdapter(notaAdapter);
        }

        private void setupApiService() {
            apiService = ApiCliente.getClient().create(ApiService.class);
        }

        private void obtenerDatosUsuario() {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                usuarioId = extras.getInt("usuarioId", 0);
                String nombre = extras != null ? extras.getString("nombre", "Usuario") : "Usuario";
                String apellido = extras != null ? extras.getString("apellido", "") : "";
                tvWelcome.setText("Bienvenido " + nombre + " " + apellido);
            } else {
                Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        private void cargarCursos() {
            apiService.getCursosPorUsuario(usuarioId).enqueue(new Callback<List<Curso>>() {
                @Override
                public void onResponse(@NonNull Call<List<Curso>> call, @NonNull Response<List<Curso>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        listaCursos.clear();
                        listaCursos.addAll(response.body());
                        cursoAdapter.notifyDataSetChanged();
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

        private void onCursoSelected(Curso curso) {
            cargarNotas(curso.getIdCurso());
        }

        private void cargarNotas(int idCurso) {
            apiService.getNotasPorCurso(usuarioId, idCurso).enqueue(new Callback<List<Nota>>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<List<Nota>> call, @NonNull Response<List<Nota>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("API_RESPONSE", new Gson().toJson(response.body()));
                        listaNotas.clear();
                        listaNotas.addAll(response.body());
                        notaAdapter.notifyDataSetChanged();
                    } else {
                        mostrarMensajeError("Error al cargar notas");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Nota>> call, @NonNull Throwable t) {
                    mostrarMensajeError("Error de conexión");
                }
            });
        }

        private void mostrarMensajeError(String mensaje) {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        }
    }