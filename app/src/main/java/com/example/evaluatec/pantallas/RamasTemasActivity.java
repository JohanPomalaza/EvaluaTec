package com.example.evaluatec.pantallas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.adaptadores.RamasAdapter;
import com.example.evaluatec.adaptadores.TemasAdapter;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.RamaCurso;
import com.example.evaluatec.modelos.TemaCurso;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RamasTemasActivity extends AppCompatActivity {
    private RecyclerView recyclerRamas, recyclerTemas;
    private Button btnAgregarTema;
    private FloatingActionButton btnAgregarRama;

    private RamasAdapter ramasAdapter;
    private TemasAdapter temasAdapter;

    private List<RamaCurso> listaRamas = new ArrayList<>();
    private List<TemaCurso> listaTemas = new ArrayList<>();

    private ApiService apiService;
    private int cursoId;
    private RamaCurso ramaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ramas_temas);

        // Inicialización de vistas
        recyclerRamas = findViewById(R.id.recyclerRamas);
        recyclerTemas = findViewById(R.id.recyclerTemas);
        btnAgregarTema = findViewById(R.id.btnAgregarTema);
        btnAgregarRama = findViewById(R.id.btnAgregarRama);

        // API y datos
        apiService = ApiCliente.getClient().create(ApiService.class);
        cursoId = getIntent().getIntExtra("cursoId", 0);

        // Adaptadores
        ramasAdapter = new RamasAdapter(listaRamas, new RamasAdapter.OnRamaListener() {
            @Override
            public void onEditar(RamaCurso rama) {
                onEditarRama(rama);
            }

            @Override
            public void onEliminar(RamaCurso rama) {
                // Lógica de eliminación aquí si la implementas
            }

            @Override
            public void onVerTemas(RamaCurso rama) {
                ramaSeleccionada = rama;
                mostrarTemasDeRama(rama.getIdRama());
            }
        });

        temasAdapter = new TemasAdapter(listaTemas, new TemasAdapter.OnTemaActionListener() {
            @Override
            public void onEditarTema(TemaCurso tema) {
                // Aquí llamas a una función para editar el tema
                onEditarTema(tema);
            }

            @Override
            public void onEliminarTema(TemaCurso tema) {
                eliminarTema(tema);
            }
        });

        // Configurar RecyclerViews
        recyclerRamas.setLayoutManager(new LinearLayoutManager(this));
        recyclerTemas.setLayoutManager(new LinearLayoutManager(this));
        recyclerRamas.setAdapter(ramasAdapter);
        recyclerTemas.setAdapter(temasAdapter);

        // Cargar ramas al iniciar
        cargarRamas();

        // Acciones de botones
        btnAgregarRama.setOnClickListener(v -> mostrarDialogoCrearRama());

        btnAgregarTema.setOnClickListener(v -> {
            if (ramaSeleccionada != null) {
                mostrarDialogoCrearTema();
            } else {
                Toast.makeText(this, "Selecciona una rama primero", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarRamas() {
        apiService.getRamasPorCurso(cursoId).enqueue(new Callback<List<RamaCurso>>() {
            @Override
            public void onResponse(Call<List<RamaCurso>> call, Response<List<RamaCurso>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaRamas.clear();
                    listaRamas.addAll(response.body());
                    ramasAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<RamaCurso>> call, Throwable t) {
                Toast.makeText(RamasTemasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarTemasDeRama(int idRama) {
        apiService.getTemasPorRama(idRama).enqueue(new Callback<List<TemaCurso>>() {
            @Override
            public void onResponse(Call<List<TemaCurso>> call, Response<List<TemaCurso>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaTemas.clear();
                    listaTemas.addAll(response.body());
                    temasAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<TemaCurso>> call, Throwable t) {
                Toast.makeText(RamasTemasActivity.this, "Error al obtener temas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoCrearRama() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nueva Rama");

        final EditText input = new EditText(this);
        input.setHint("Nombre de la rama");
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = input.getText().toString().trim();
            if (!nombre.isEmpty()) {
                RamaCurso nueva = new RamaCurso();
                nueva.setNombreRama(nombre);
                nueva.setIdCurso(cursoId);

                apiService.crearRama(nueva).enqueue(new Callback<RamaCurso>() {
                    @Override
                    public void onResponse(Call<RamaCurso> call, Response<RamaCurso> response) {
                        if (response.isSuccessful()) {
                            cargarRamas();
                            Toast.makeText(RamasTemasActivity.this, "Rama agregada", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RamaCurso> call, Throwable t) {
                        Toast.makeText(RamasTemasActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void mostrarDialogoCrearTema() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Tema");

        final EditText input = new EditText(this);
        input.setHint("Nombre del tema");
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombreTema = input.getText().toString().trim();
            if (!nombreTema.isEmpty()) {
                TemaCurso nuevoTema = new TemaCurso();
                nuevoTema.setNombre(nombreTema);
                nuevoTema.setIdRama(ramaSeleccionada.getIdRama());
                crearTema(nuevoTema);
            } else {
                Toast.makeText(this, "Ingresa un nombre válido", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void crearTema(TemaCurso tema) {
        apiService.crearTema(tema).enqueue(new Callback<TemaCurso>() {
            @Override
            public void onResponse(Call<TemaCurso> call, Response<TemaCurso> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaTemas.add(response.body());
                    temasAdapter.notifyItemInserted(listaTemas.size() - 1);
                    Toast.makeText(RamasTemasActivity.this, "Tema agregado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RamasTemasActivity.this, "Error al agregar tema", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TemaCurso> call, Throwable t) {
                Toast.makeText(RamasTemasActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onEditarRama(RamaCurso rama) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Rama");

        final EditText input = new EditText(this);
        input.setText(rama.getNombreRama());
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoNombre = input.getText().toString().trim();
            if (!nuevoNombre.isEmpty()) {
                rama.setNombreRama(nuevoNombre);
                apiService.editarRama(rama.getIdRama(), rama).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            cargarRamas();
                            Toast.makeText(RamasTemasActivity.this, "Rama actualizada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RamasTemasActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(RamasTemasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    private void onEditarTema(TemaCurso tema) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Tema");

        final EditText input = new EditText(this);
        input.setText(tema.getNombre());
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoNombre = input.getText().toString().trim();
            if (!nuevoNombre.isEmpty()) {
                tema.setNombre(nuevoNombre);
                apiService.editarTema(tema.getIdTema(), tema).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(RamasTemasActivity.this, "Tema actualizado", Toast.LENGTH_SHORT).show();
                            mostrarTemasDeRama(tema.getIdRama());
                        } else {
                            Toast.makeText(RamasTemasActivity.this, "Error al actualizar tema", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(RamasTemasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    private void eliminarTema(TemaCurso tema) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Tema")
                .setMessage("¿Estás seguro de eliminar este tema?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    apiService.eliminarTema(tema.getIdTema()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(RamasTemasActivity.this, "Tema eliminado", Toast.LENGTH_SHORT).show();
                                mostrarTemasDeRama(tema.getIdRama());
                            } else {
                                Toast.makeText(RamasTemasActivity.this, "Error al eliminar tema", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(RamasTemasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}