package com.example.evaluatec.pantallas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.NotificacionesActivity;
import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.modelos.NotaPorCurso;
import com.example.evaluatec.modelos.NotificacionDto;
import com.google.android.material.navigation.NavigationBarView;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.Curso;
import com.example.evaluatec.modelos.Nota;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        private List<NotaPorCurso> listaNotas = new ArrayList<>();
        private ApiService apiService;
        private int usuarioId;
        TextView badgeNotificaciones;
        ImageButton btnNotificaciones;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_estudiante_activity);

            initViews();
            setupRecyclerViews();
            setupApiService();
            obtenerDatosUsuario();
            obtenerCantidadNoLeidas();
            cargarCursos();
            setupBottomNavigation();
        }

        private void initViews() {
            tvWelcome = findViewById(R.id.tvWelcome);
            bottomNav = findViewById(R.id.bottom_navigation);
            rvCursos = findViewById(R.id.rvCursos);
            rvDetalleCursos = findViewById(R.id.rvDetalleCursos);
            badgeNotificaciones = findViewById(R.id.badgeNotificaciones);
            btnNotificaciones = findViewById(R.id.btnNotificaciones);


            btnNotificaciones.setOnClickListener(v -> {
                Intent intent = new Intent(this, NotificacionesActivity.class);
                intent.putExtra("usuarioId", usuarioId);
                startActivity(intent);
            });

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
            notaAdapter = new NotaAdapter(listaNotas, this);
            rvDetalleCursos.setLayoutManager(new LinearLayoutManager(this));
            rvDetalleCursos.setAdapter(notaAdapter);

            rvDetalleCursos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
            apiService.getCursosPorAlumno(usuarioId).enqueue(new Callback<List<Curso>>() {
                @Override
                public void onResponse(@NonNull Call<List<Curso>> call, @NonNull Response<List<Curso>> response) {
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

                        CursoAdapter cursoAdapter = new CursoAdapter(cursosUnicos, EstudianteActivity.this::onCursoSelected);
                        rvCursos.setAdapter(cursoAdapter);


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
            apiService.getNotasPorCurso(usuarioId, idCurso).enqueue(new Callback<List<NotaPorCurso>>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<List<NotaPorCurso>> call, @NonNull Response<List<NotaPorCurso>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("API_RESPONSE", new Gson().toJson(response.body()));
                        listaNotas.clear();
                        listaNotas.addAll(response.body());
                        notaAdapter.actualizarLista(listaNotas);
                    } else {
                        mostrarMensajeError("Error al cargar notas");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<NotaPorCurso>> call, @NonNull Throwable t) {
                    mostrarMensajeError("Error de conexión");
                }
            });
        }

        private void mostrarMensajeError(String mensaje) {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        }
        private void obtenerCantidadNoLeidas() {
            apiService.getNotificacionesNoLeidas(usuarioId).enqueue(new Callback<List<NotificacionDto>>() {
                @Override
                public void onResponse(Call<List<NotificacionDto>> call, Response<List<NotificacionDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int cantidad = response.body().size();
                        runOnUiThread(() -> {
                            if (cantidad > 0) {
                                badgeNotificaciones.setText(String.valueOf(cantidad));
                                badgeNotificaciones.setVisibility(View.VISIBLE);
                            } else {
                                badgeNotificaciones.setVisibility(View.GONE);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<NotificacionDto>> call, Throwable t) {
                    // Silenciar o mostrar error
                }
            });
        }
        @Override
        protected void onResume() {
            super.onResume();
            obtenerCantidadNoLeidas();
        }
    }