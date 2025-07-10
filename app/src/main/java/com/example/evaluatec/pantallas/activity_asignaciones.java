package com.example.evaluatec.pantallas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.adaptadores.AsignacionAdapter;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.AnioEscolarDto;
import com.example.evaluatec.modelos.AsignacionActualizarDto;
import com.example.evaluatec.modelos.AsignacionCrearDto;
import com.example.evaluatec.modelos.AsignacionDto;
import com.example.evaluatec.modelos.CursoDto;
import com.example.evaluatec.modelos.GradoDto;
import com.example.evaluatec.modelos.GradoSeccionDto;
import com.example.evaluatec.modelos.NivelDto;
import com.example.evaluatec.modelos.RamaDto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<RamaDto> listaRamas;
    private List<Integer> ramasSeleccionadasIds = new ArrayList<>();


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

        TextView txtApartado = findViewById(R.id.txtApartado);
        txtApartado.setText("Asignaciones de " + nombreDocente);

        fabAgregar.setOnClickListener(v -> mostrarDialogoAsignacion(null));

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
                            mostrarDialogoAsignacion(asignacion);
                        }
                        @Override
                        public void onEliminar(AsignacionDto asignacion) {
                            mostrarDialogoEliminarAsignacion(asignacion); // ← Puedes implementar este método
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


    private void mostrarDialogoAsignacion(@Nullable AsignacionDto asignacionEditar) {
        boolean esEdicion = asignacionEditar != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(esEdicion ? "Editar Asignación" : "Nueva Asignación");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_asignacion, null);
        Spinner spinnerNivel = view.findViewById(R.id.spinnerNivel);
        Spinner spinnerGradoSeccion = view.findViewById(R.id.spinnerGradoSeccion);
        Spinner spinnerCurso = view.findViewById(R.id.spinnerCurso);
        Spinner spinnerAnio = view.findViewById(R.id.spinnerAnioEscolar);
        TextView txtRamasSeleccionadas = view.findViewById(R.id.txtRamasSeleccionadas);

        builder.setView(view);

        cargarNiveles(spinnerNivel);
        cargarAniosEscolares(spinnerAnio);

        spinnerNivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NivelDto nivel = (NivelDto) parent.getItemAtPosition(position);
                int idNivel = nivel.getIdNivel();

                // Cargar grados y secciones según nivel
                cargarGradosSeccionesPorNivel(spinnerGradoSeccion, idNivel);

                // Cargar cursos según nivel
                cargarCursosPorNivel(spinnerCurso, idNivel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerCurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CursoDto curso = (CursoDto) parent.getItemAtPosition(position);
                cargarRamasPorCurso(curso.getIdCurso(), txtRamasSeleccionadas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        if (esEdicion) {

            int idNivel = asignacionEditar.getIdNivel();
            seleccionarEnSpinnerPorId(spinnerNivel, idNivel);
            cargarGradosSeccionesPorNivel(spinnerGradoSeccion, idNivel);
            cargarCursosPorNivel(spinnerCurso, idNivel);

            spinnerCurso.post(() -> {
                seleccionarEnSpinnerPorId(spinnerCurso, asignacionEditar.getIdCurso());
                cargarRamasPorCurso(asignacionEditar.getIdCurso(), txtRamasSeleccionadas);
            });

            seleccionarEnSpinnerPorId(spinnerAnio, asignacionEditar.getIdAnioEscolar());
        }

        builder.setPositiveButton(esEdicion ? "Actualizar" : "Guardar", (dialog, which) -> {
            GradoSeccionDto gradoSeccion = (GradoSeccionDto) spinnerGradoSeccion.getSelectedItem();
            AnioEscolarDto anio = (AnioEscolarDto) spinnerAnio.getSelectedItem();
            CursoDto curso = (CursoDto) spinnerCurso.getSelectedItem();

            if (gradoSeccion == null || anio == null || ramasSeleccionadasIds.isEmpty()) {
                Toast.makeText(this, "Debe seleccionar ramas, grado y año escolar", Toast.LENGTH_SHORT).show();
                return;
            }

            if (esEdicion) {
                int idRamaActualizada = ramasSeleccionadasIds.get(0); // Solo se edita una por vez
                AsignacionActualizarDto dto = new AsignacionActualizarDto(idRamaActualizada, gradoSeccion.getIdGrado(), gradoSeccion.getIdSeccion());

                apiService.actualizarAsignacion(asignacionEditar.getIdAsignacion(), dto).enqueue(new Callback<Void>() {
                    @Override public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) cargarAsignaciones();
                        else Toast.makeText(activity_asignaciones.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(activity_asignaciones.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                AsignacionCrearDto dto = new AsignacionCrearDto();
                dto.setIdAsignador(usuarioId);
                dto.setIdGrado(gradoSeccion.getIdGrado());
                dto.setIdSeccion(gradoSeccion.getIdSeccion());
                dto.setIdAnioEscolar(anio.getIdAnioEscolar());
                dto.setIdRamas(ramasSeleccionadasIds);

                apiService.asignarCurso(idDocente, dto).enqueue(new Callback<Void>() {
                    @Override public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) cargarAsignaciones();
                        else Toast.makeText(activity_asignaciones.this, "Error al asignar", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(activity_asignaciones.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    private void seleccionarEnSpinnerPorId(Spinner spinner, int idBuscado) {
        ArrayAdapter<?> adapter = (ArrayAdapter<?>) spinner.getAdapter();
        if (adapter == null) return;

        for (int i = 0; i < adapter.getCount(); i++) {
            Object item = adapter.getItem(i);
            if (item instanceof AsignacionDto.HasId && ((AsignacionDto.HasId) item).getId() == idBuscado) {
                spinner.setSelection(i);
                break;
            }
        }
    }
    private void cargarNiveles(Spinner spinner) {
        apiService.getNiveles().enqueue(new Callback<List<NivelDto>>() {
            @Override
            public void onResponse(Call<List<NivelDto>> call, Response<List<NivelDto>> response) {
                if (response.isSuccessful()) {
                    List<NivelDto> lista = response.body();
                    ArrayAdapter<NivelDto> adapter = new ArrayAdapter<>(activity_asignaciones.this, android.R.layout.simple_spinner_item, lista);
                    spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<NivelDto>> call, Throwable t) {}
        });
    }

    private void cargarCursosPorNivel(Spinner spinner, int idNivel) {
        apiService.getCursosPorNivel(idNivel).enqueue(new Callback<List<CursoDto>>() {
            @Override
            public void onResponse(Call<List<CursoDto>> call, Response<List<CursoDto>> response) {
                if (response.isSuccessful()) {
                    List<CursoDto> lista = response.body();
                    ArrayAdapter<CursoDto> adapter = new ArrayAdapter<>(activity_asignaciones.this, android.R.layout.simple_spinner_item, lista);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<CursoDto>> call, Throwable t) {
                Toast.makeText(activity_asignaciones.this, "Error al cargar cursos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarGradosSeccionesPorNivel(Spinner spinner, int idNivel) {
        apiService.getGradosYSeccionesPorNivel(idNivel).enqueue(new Callback<>() {
            public void onResponse(Call<List<GradoSeccionDto>> call, Response<List<GradoSeccionDto>> response) {
                if (response.isSuccessful()) {
                    ArrayAdapter<GradoSeccionDto> adapter = new ArrayAdapter<>(activity_asignaciones.this, android.R.layout.simple_spinner_item, response.body());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
            }
            public void onFailure(Call<List<GradoSeccionDto>> call, Throwable t) {}
        });
    }

    private void cargarRamasPorCurso(int idCurso, TextView txtRamas) {
        apiService.getRamasPorCursoDoc(idCurso).enqueue(new Callback<>() {
            public void onResponse(Call<List<RamaDto>> call, Response<List<RamaDto>> response) {
                if (response.isSuccessful()) {
                    listaRamas = response.body();
                    txtRamas.setOnClickListener(v -> mostrarSelectorRamas(txtRamas));
                }
            }
            public void onFailure(Call<List<RamaDto>> call, Throwable t) {}
        });
    }

    private void mostrarSelectorRamas(TextView txtRamas) {
        if (listaRamas == null || listaRamas.isEmpty()) return;

        String[] nombres = new String[listaRamas.size()];
        boolean[] seleccionados = new boolean[listaRamas.size()];

        for (int i = 0; i < listaRamas.size(); i++) {
            nombres[i] = listaRamas.get(i).getNombre();
            seleccionados[i] = ramasSeleccionadasIds.contains(listaRamas.get(i).getIdRama());
        }

        new AlertDialog.Builder(this)
                .setTitle("Seleccionar Ramas")
                .setMultiChoiceItems(nombres, seleccionados, (dialog, which, isChecked) -> {
                    int idRama = listaRamas.get(which).getIdRama();
                    if (isChecked && !ramasSeleccionadasIds.contains(idRama)) {
                        ramasSeleccionadasIds.add(idRama);
                    } else if (!isChecked) {
                        ramasSeleccionadasIds.remove(Integer.valueOf(idRama));
                    }
                })
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    StringBuilder texto = new StringBuilder();
                    for (int i = 0; i < seleccionados.length; i++) {
                        if (seleccionados[i]) texto.append(nombres[i]).append(", ");
                    }
                    if (texto.length() > 0) texto.setLength(texto.length() - 2);
                    txtRamas.setText(texto.toString());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        Log.e("Asignaciones", mensaje);
    }

    private void cargarAniosEscolares(Spinner spinner) {
        apiService.getAniosEscolares().enqueue(new Callback<>() {
            public void onResponse(Call<List<AnioEscolarDto>> call, Response<List<AnioEscolarDto>> response) {
                if (response.isSuccessful()) {
                    ArrayAdapter<AnioEscolarDto> adapter = new ArrayAdapter<>(activity_asignaciones.this, android.R.layout.simple_spinner_item, response.body());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
            }
            public void onFailure(Call<List<AnioEscolarDto>> call, Throwable t) {}
        });
    }
    private void mostrarDialogoEliminarAsignacion(AsignacionDto asignacion) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Asignación")
                .setMessage("¿Estás seguro de que deseas eliminar esta asignación?")
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    apiService.eliminarAsignacion(asignacion.getIdAsignacion()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(activity_asignaciones.this, "Asignación eliminada", Toast.LENGTH_SHORT).show();
                                cargarAsignaciones();
                            } else {
                                Toast.makeText(activity_asignaciones.this, "Error al eliminar la asignación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(activity_asignaciones.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}