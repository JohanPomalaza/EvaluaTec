package com.example.evaluatec.pantallas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.adaptadores.AsignacionAdapter;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.AsignacionActualizarDto;
import com.example.evaluatec.modelos.AsignacionCrearDto;
import com.example.evaluatec.modelos.AsignacionDto;
import com.example.evaluatec.modelos.GradoDto;
import com.example.evaluatec.modelos.RamaDto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_asignaciones extends AppCompatActivity {
    private RecyclerView recyclerAsignaciones;
    private FloatingActionButton fabAgregar;
    private ApiService apiService;
    private int idDocente;
    private int usuarioId;
    private String nombreDocente;
    private AsignacionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignaciones);

        recyclerAsignaciones = findViewById(R.id.recyclerAsignaciones);
        fabAgregar = findViewById(R.id.fabAgregarAsignacion);

        recyclerAsignaciones.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiCliente.getClient().create(ApiService.class);
        usuarioId = getIntent().getIntExtra("usuarioId", 0);
        idDocente = getIntent().getIntExtra("idDocente", 0);
        nombreDocente = getIntent().getStringExtra("nombreDocente");

        setTitle("Asignaciones de " + nombreDocente);

        fabAgregar.setOnClickListener(v -> mostrarDialogoAsignar(null));

        cargarAsignaciones();
    }

    private void cargarAsignaciones() {
        apiService.getAsignacionesPorDocente(idDocente).enqueue(new Callback<List<AsignacionDto>>() {
            @Override
            public void onResponse(Call<List<AsignacionDto>> call, Response<List<AsignacionDto>> response) {
                if (response.isSuccessful()) {
                    List<AsignacionDto> lista = response.body();
                    adapter = new AsignacionAdapter(lista, new AsignacionAdapter.OnAsignacionActionListener() {
                        @Override
                        public void onEditar(AsignacionDto asignacion) {
                            mostrarDialogoAsignar(asignacion);
                        }
                    });
                    recyclerAsignaciones.setAdapter(adapter);
                } else {
                    Toast.makeText(activity_asignaciones.this, "Error al cargar asignaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AsignacionDto>> call, Throwable t) {
                Toast.makeText(activity_asignaciones.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoAsignar(AsignacionDto asignacionExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(asignacionExistente == null ? "Nueva Asignación" : "Editar Asignación");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_asignacion, null);
        Spinner spinnerCurso = view.findViewById(R.id.spinnerCurso);
        Spinner spinnerSeccion = view.findViewById(R.id.spinnerSeccion);

        builder.setView(view);

        cargarRamas(spinnerCurso, asignacionExistente != null ? asignacionExistente.getIdRamaCurso() : -1);
        cargarGrados(spinnerSeccion, asignacionExistente != null ? asignacionExistente.getIdGrado() : -1);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            RamaDto rama = (RamaDto) spinnerCurso.getSelectedItem();
            GradoDto grado = (GradoDto) spinnerSeccion.getSelectedItem();

            if (rama != null && grado != null) {
                if (asignacionExistente == null) {
                    AsignacionCrearDto nuevo = new AsignacionCrearDto(idDocente, usuarioId, rama.getIdRama(), grado.getIdGrado(), 4);
                    apiService.asignarCurso(idDocente, nuevo).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(activity_asignaciones.this, "Asignación creada", Toast.LENGTH_SHORT).show();
                                cargarAsignaciones();
                            } else {
                                Toast.makeText(activity_asignaciones.this, "Error al crear asignación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(activity_asignaciones.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    AsignacionActualizarDto actualizar = new AsignacionActualizarDto(rama.getIdRama(), grado.getIdGrado());
                    apiService.actualizarAsignacion(asignacionExistente.getIdAsignacion(), actualizar).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(activity_asignaciones.this, "Asignación actualizada", Toast.LENGTH_SHORT).show();
                                cargarAsignaciones();
                            } else {
                                Toast.makeText(activity_asignaciones.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(activity_asignaciones.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void cargarRamas(Spinner spinner, int idSeleccionado) {
        apiService.getRamas().enqueue(new Callback<List<RamaDto>>() {
            @Override
            public void onResponse(Call<List<RamaDto>> call, Response<List<RamaDto>> response) {
                if (response.isSuccessful()) {
                    List<RamaDto> lista = response.body();
                    ArrayAdapter<RamaDto> adapter = new ArrayAdapter<>(activity_asignaciones.this, android.R.layout.simple_spinner_item, lista);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    if (idSeleccionado != -1) {
                        for (int i = 0; i < lista.size(); i++) {
                            if (lista.get(i).getIdRama() == idSeleccionado) {
                                spinner.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RamaDto>> call, Throwable t) {
                Toast.makeText(activity_asignaciones.this, "Error al cargar cursos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarGrados(Spinner spinner, int idSeleccionado) {
        apiService.getGrados().enqueue(new Callback<List<GradoDto>>() {
            @Override
            public void onResponse(Call<List<GradoDto>> call, Response<List<GradoDto>> response) {
                if (response.isSuccessful()) {
                    List<GradoDto> lista = response.body();
                    ArrayAdapter<GradoDto> adapter = new ArrayAdapter<>(activity_asignaciones.this, android.R.layout.simple_spinner_item, lista);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    if (idSeleccionado != -1) {
                        for (int i = 0; i < lista.size(); i++) {
                            if (lista.get(i).getIdGrado() == idSeleccionado) {
                                spinner.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GradoDto>> call, Throwable t) {
                Toast.makeText(activity_asignaciones.this, "Error al cargar grados", Toast.LENGTH_SHORT).show();
            }
        });
    }
}