package com.example.evaluatec.pantallas;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.adaptadores.EstudianteManteAdapter;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.AnioEscolarDto;
import com.example.evaluatec.modelos.AsignacionGradoDto;
import com.example.evaluatec.modelos.DocenteDto;
import com.example.evaluatec.modelos.Estudiante;
import com.example.evaluatec.modelos.EstudianteCrearDto;
import com.example.evaluatec.modelos.EstudianteDto;
import com.example.evaluatec.modelos.GradoDto;
import com.example.evaluatec.modelos.GradoSeccionDto;
import com.example.evaluatec.modelos.HistorialEstudiante;
import com.example.evaluatec.modelos.HistorialTema;
import com.example.evaluatec.modelos.NivelDto;
import com.example.evaluatec.modelos.TemaCurso;
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
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes_mantenimiento);

        recyclerView = findViewById(R.id.recyclerEstudiantes);

        fab = findViewById(R.id.fabAgregarEstudiante);
        apiService = ApiCliente.getClient().create(ApiService.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        idUsuario = getIntent().getIntExtra("usuarioId", 0);

        adapter = new EstudianteManteAdapter(listaEstudiantes, new EstudianteManteAdapter.OnEstudianteListener() {
            @Override
            public void onEditar(EstudianteDto estudiante) {
                mostrarDialogoEstudiante(estudiante);
            }

            @Override
            public void onAsignarGrado(EstudianteDto estudiante) {
                mostrarDialogoAsignarGrado(estudiante);
            }
            @Override
            public void onEliminar(EstudianteDto estudiante) {
                mostrarDialogoConfirmacionEliminar(estudiante.getIdUsuario());
            }
            @Override
            public void onVerHistorialEstudiante(EstudianteDto estudiante) {
                verHistorialEstudiante(estudiante, idUsuario);
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
            edtCorreo.setText(estudiante.getCorreo());
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
                        Toast.makeText(EstudiantesMantenimientoActivity.this, "Error al crear y/o el correo ingresado ya existe", Toast.LENGTH_SHORT).show();
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
        Spinner spinnerNivel = view.findViewById(R.id.spinnerNivel);
        Spinner spinnerGradoSeccion = view.findViewById(R.id.spinnerGradoSeccion);
        Spinner spinnerAnioEscolar = view.findViewById(R.id.spinnerAnioEscolar);

        List<NivelDto> niveles = new ArrayList<>();
        List<GradoSeccionDto> gradosSecciones = new ArrayList<>();
        List<AnioEscolarDto> aniosEscolares = new ArrayList<>();

        ArrayAdapter<NivelDto> adapterNivel = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, niveles);
        adapterNivel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNivel.setAdapter(adapterNivel);

        ArrayAdapter<GradoSeccionDto> adapterGradoSeccion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradosSecciones);
        adapterGradoSeccion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGradoSeccion.setAdapter(adapterGradoSeccion);

        ArrayAdapter<AnioEscolarDto> adapterAnio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aniosEscolares);
        adapterAnio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnioEscolar.setAdapter(adapterAnio);

        // Cargar niveles
        apiService.getNiveles().enqueue(new Callback<List<NivelDto>>() {
            @Override
            public void onResponse(Call<List<NivelDto>> call, Response<List<NivelDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    niveles.clear();
                    niveles.addAll(response.body());
                    adapterNivel.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<NivelDto>> call, Throwable t) {}
        });

        // Al seleccionar un nivel, cargar grados + secciones
        spinnerNivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NivelDto nivelSeleccionado = niveles.get(position);
                apiService.getGradosYSeccionesPorNivel(nivelSeleccionado.getIdNivel()).enqueue(new Callback<List<GradoSeccionDto>>() {
                    @Override
                    public void onResponse(Call<List<GradoSeccionDto>> call, Response<List<GradoSeccionDto>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            gradosSecciones.clear();
                            gradosSecciones.addAll(response.body());
                            adapterGradoSeccion.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GradoSeccionDto>> call, Throwable t) {}
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Cargar años escolares
        apiService.getAniosEscolares().enqueue(new Callback<List<AnioEscolarDto>>() {
            @Override
            public void onResponse(Call<List<AnioEscolarDto>> call, Response<List<AnioEscolarDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    aniosEscolares.clear();
                    aniosEscolares.addAll(response.body());
                    adapterAnio.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<AnioEscolarDto>> call, Throwable t) {}
        });

        builder.setView(view);
        builder.setPositiveButton("Asignar", (dialog, which) -> {
            int posGrado = spinnerGradoSeccion.getSelectedItemPosition();
            int posAnio = spinnerAnioEscolar.getSelectedItemPosition();

            if (posGrado >= 0 && posAnio >= 0) {
                GradoSeccionDto seleccionado = gradosSecciones.get(posGrado);
                int idGrado = seleccionado.getIdGrado();
                int idAnio = aniosEscolares.get(posAnio).getIdAnioEscolar();
                int idSeccion = seleccionado.getIdSeccion();
                int usuarioResponsable = idUsuario;

                AsignacionGradoDto dto = new AsignacionGradoDto(idGrado, idAnio, idSeccion,usuarioResponsable);
                apiService.asignarGrado(estudiante.getIdUsuario(), dto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EstudiantesMantenimientoActivity.this, "Asignación realizada", Toast.LENGTH_SHORT).show();
                            cargarEstudiantes();
                        } else {
                            Toast.makeText(EstudiantesMantenimientoActivity.this, "Error al asignar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(EstudiantesMantenimientoActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Seleccione grado y año escolar", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    private void mostrarDialogoConfirmacionEliminar(int idEstudiante) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar")
                .setMessage("¿Deseas inactivar este estudiante?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    apiService.inactivarEstudiante(idEstudiante).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Estudiante inactivado", Toast.LENGTH_SHORT).show();
                                cargarEstudiantes();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error al inactivar", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Error de red", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void verHistorialEstudiante(EstudianteDto estudianteDto, int idUsuario) {
        apiService.getHistorialEstudiante(estudianteDto.getIdUsuario()).enqueue(new Callback<List<HistorialEstudiante>>() {
            @Override
            public void onResponse(Call<List<HistorialEstudiante>> call, Response<List<HistorialEstudiante>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<HistorialEstudiante> historialEstudiante= response.body();
                    mostrarDialogoHistorialEstudiante(historialEstudiante);
                } else {
                    Toast.makeText(EstudiantesMantenimientoActivity.this, "No se pudo obtener el historial", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HistorialEstudiante>> call, Throwable t) {
                Toast.makeText(EstudiantesMantenimientoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mostrarDialogoHistorialEstudiante(List<HistorialEstudiante> historialEstudiante) {
        Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Historial del Estudiante");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_historial_estudiante, null);
        LinearLayout layoutEstudiante = view.findViewById(R.id.layoutEstudiante);

        if (historialEstudiante != null && !historialEstudiante.isEmpty()) {
            for (HistorialEstudiante historial : historialEstudiante) {
                CardView card = new CardView(context);
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(0, 8, 0, 8);
                card.setLayoutParams(cardParams);
                card.setRadius(16f);
                card.setCardElevation(6f);
                card.setUseCompatPadding(true);

                LinearLayout container = new LinearLayout(context);
                container.setOrientation(LinearLayout.VERTICAL);
                container.setPadding(24, 24, 24, 24);

                TextView tvAccion = crearText("Acción: ", historial.getAccion(), true);
                TextView tvGradoNombre = crearText("Grado: ", historial.getGradoNombre(), false);
                TextView tvSeccionNombre = crearText("Seccion: ", historial.getSeccionNombre(), false);
                TextView tvFecha = crearText("Fecha: ", historial.getFechaCambio(), false);
                TextView tvUsuario = crearText("Usuario responsable: ", historial.getNombreResponsable(), false);

                container.addView(tvAccion);
                container.addView(tvGradoNombre);
                container.addView(tvSeccionNombre);
                container.addView(tvFecha);
                container.addView(tvUsuario);

                card.addView(container);
                layoutEstudiante.addView(card);
            }
        } else {
            TextView tv = new TextView(context);
            tv.setText("No hay historial para este Estudiante.");
            tv.setPadding(0, 20, 0, 20);
            tv.setTextSize(16);
            tv.setTextColor(Color.GRAY);
            layoutEstudiante.addView(tv);
        }

        builder.setView(view);
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.teal_700));
    }
    private TextView crearText(String label, String value, boolean boldTitle) {
        Context context = this;
        TextView textView = new TextView(context);
        SpannableString spannable = new SpannableString(label + value);
        if (boldTitle) {
            spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannable);
        textView.setTextSize(15);
        textView.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        textView.setPadding(0, 4, 0, 4);
        return textView;
    }
}