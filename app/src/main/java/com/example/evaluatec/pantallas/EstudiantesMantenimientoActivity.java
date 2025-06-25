package com.example.evaluatec.pantallas;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.adaptadores.EstudianteManteAdapter;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.AsignacionGradoDto;
import com.example.evaluatec.modelos.EstudianteCrearDto;
import com.example.evaluatec.modelos.EstudianteDto;
import com.example.evaluatec.modelos.GradoDto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstudiantesMantenimientoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ApiService apiService;
    private EstudianteManteAdapter adapter;
    private List<EstudianteDto> listaEstudiantes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes_mantenimiento);

        recyclerView = findViewById(R.id.recyclerEstudiantes);

        fab = findViewById(R.id.fabAgregarEstudiante);
        apiService = ApiCliente.getClient().create(ApiService.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EstudianteManteAdapter(listaEstudiantes, new EstudianteManteAdapter.OnEstudianteListener() {
            @Override
            public void onEditar(EstudianteDto estudiante) {
                mostrarDialogoEstudiante(estudiante);
            }

            @Override
            public void onAsignarGrado(EstudianteDto estudiante) {
                mostrarDialogoAsignarGrado(estudiante);
            }
        });
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> mostrarDialogoEstudiante(null));

        cargarEstudiantes();
    }

    private void cargarEstudiantes() {
        apiService.obtenerEstudiantes().enqueue(new Callback<List<EstudianteDto>>() {
            @Override
            public void onResponse(Call<List<EstudianteDto>> call, Response<List<EstudianteDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaEstudiantes.clear();
                    listaEstudiantes.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<EstudianteDto>> call, Throwable t) {
                Toast.makeText(EstudiantesMantenimientoActivity.this, "Error al cargar estudiantes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoEstudiante(EstudianteDto estudiante) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(estudiante == null ? "Agregar Estudiante" : "Editar Estudiante");

        View view = getLayoutInflater().inflate(R.layout.dialog_estudiante, null);
        EditText edtNombre = view.findViewById(R.id.edtNombre);
        EditText edtApellido = view.findViewById(R.id.edtApellido);
        EditText edtCorreo = view.findViewById(R.id.edtCorreo);
        EditText edtContrasena = view.findViewById(R.id.edtContrasena);

        if (estudiante != null) {
            edtNombre.setText(estudiante.getNombre());
            edtApellido.setText(estudiante.getApellido());
        }

        builder.setView(view);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            EstudianteCrearDto dto = new EstudianteCrearDto(
                    edtNombre.getText().toString(),
                    edtApellido.getText().toString(),
                    edtCorreo.getText().toString(),
                    edtContrasena.getText().toString()
            );

            if (estudiante == null) {
                apiService.crearEstudiante(dto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EstudiantesMantenimientoActivity.this, "Estudiante creado", Toast.LENGTH_SHORT).show();
                            cargarEstudiantes();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(EstudiantesMantenimientoActivity.this, "Error al crear", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                apiService.editarEstudiante(estudiante.getIdUsuario(), dto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EstudiantesMantenimientoActivity.this, "Estudiante actualizado", Toast.LENGTH_SHORT).show();
                            cargarEstudiantes();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(EstudiantesMantenimientoActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    private void mostrarDialogoAsignarGrado(EstudianteDto estudiante) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Asignar Grado");

        View view = getLayoutInflater().inflate(R.layout.dialog_asignar_grado, null);
        Spinner spinnerGrado = view.findViewById(R.id.spinnerGrado);
        EditText edtAnioEscolar = view.findViewById(R.id.edtAnioEscolar);

        List<String> nombresGrados = new ArrayList<>();
        List<GradoDto> listaGrados = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombresGrados
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrado.setAdapter(adapter);

// Al cargar grados desde la API
        apiService.obtenerGrados().enqueue(new Callback<List<GradoDto>>() {
            @Override
            public void onResponse(Call<List<GradoDto>> call, Response<List<GradoDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaGrados.addAll(response.body());
                    for (GradoDto g : listaGrados) {
                        nombresGrados.add(g.getNombreGrado());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<GradoDto>> call, Throwable t) {
                Toast.makeText(EstudiantesMantenimientoActivity.this, "Error al cargar grados", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);
        builder.setPositiveButton("Asignar", (dialog, which) -> {
            int pos = spinnerGrado.getSelectedItemPosition();

            if (pos >= 0 && pos < listaGrados.size()) {
                GradoDto gradoSeleccionado = listaGrados.get(pos);

                String anioText = edtAnioEscolar.getText().toString().trim();

                if (!anioText.isEmpty()) {
                    try {
                        int idGrado = gradoSeleccionado.getIdGrado();
                        int idAnioEscolar = Integer.parseInt(anioText);

                        AsignacionGradoDto dto = new AsignacionGradoDto(idGrado, idAnioEscolar);

                        apiService.asignarGrado(estudiante.getIdUsuario(), dto).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(EstudiantesMantenimientoActivity.this, "Grado asignado", Toast.LENGTH_SHORT).show();
                                    cargarEstudiantes(); // Refresca la lista si deseas
                                } else {
                                    Toast.makeText(EstudiantesMantenimientoActivity.this, "Error al asignar grado", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(EstudiantesMantenimientoActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (NumberFormatException e) {
                        Toast.makeText(EstudiantesMantenimientoActivity.this, "El año escolar debe ser un número válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EstudiantesMantenimientoActivity.this, "Debes ingresar el año escolar", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EstudiantesMantenimientoActivity.this, "Debes seleccionar un grado", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}