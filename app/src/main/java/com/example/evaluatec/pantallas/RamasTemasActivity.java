package com.example.evaluatec.pantallas;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.adaptadores.RamasAdapter;
import com.example.evaluatec.adaptadores.TemasAdapter;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.Curso;
import com.example.evaluatec.modelos.CursoMantenimiento;
import com.example.evaluatec.modelos.HistorialCurso;
import com.example.evaluatec.modelos.HistorialRama;
import com.example.evaluatec.modelos.RamaCurso;
import com.example.evaluatec.modelos.RamaCursoCrearDTO;
import com.example.evaluatec.modelos.RamaEditarDto;
import com.example.evaluatec.modelos.RamaMantenimiento;
import com.example.evaluatec.modelos.TemaCrearDTO;
import com.example.evaluatec.modelos.TemaCurso;
import com.example.evaluatec.modelos.TemaEditarDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

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
    private String cursoNombre;
    private RamaCurso ramaSeleccionada;
    private int usuarioId;
    private int gradoSeleccionado = 0;

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
        cursoNombre = getIntent().getStringExtra("cursoNombre");
        usuarioId = getIntent().getIntExtra("usuarioId", 0);

        // Adaptadores
        ramasAdapter = new RamasAdapter(listaRamas, new RamasAdapter.OnRamaListener() {
            @Override
            public void onEditar(RamaCurso rama) {
                onEditarRama(rama, usuarioId);
            }

            @Override
            public void onEliminar(RamaCurso rama) {
                onEliminarRama(rama, usuarioId);
            }

            @Override
            public void onVerTemas(RamaCurso rama) {
                ramaSeleccionada = rama;
                mostrarTemasDeRama(rama.getIdRama());
            }

            @Override
            public void onVerHistorialRamas(RamaCurso rama) {
                verHistorialRama(rama, usuarioId);
            }
        });

        temasAdapter = new TemasAdapter(listaTemas, new TemasAdapter.OnTemaActionListener() {
            @Override
            public void onEditarTema(TemaCurso tema) {
                EditarTema(tema, usuarioId);
            }

            @Override
            public void onEliminarTema(TemaCurso tema) {
                 EliminarTema(tema, usuarioId);
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

                    List<TemaCurso> temas = response.body();
                    listaTemas.addAll(temas);
                    temasAdapter.notifyDataSetChanged();

                    if (!temas.isEmpty()) {
                        TemaCurso primerTema = temas.get(0);

                        if (primerTema.getGrado() != null && primerTema.getGrado().getIdGrado() > 0) {
                            gradoSeleccionado = primerTema.getGrado().getIdGrado();
                            Log.d("API_DEBUG", "Grado seleccionado: " + gradoSeleccionado);
                        } else if (primerTema.getIdGrado() > 0) {
                            // Si no tiene objeto Grado pero tiene idGrado suelto
                            gradoSeleccionado = primerTema.getIdGrado();
                            Log.d("API_DEBUG", "Grado seleccionado (id suelto): " + gradoSeleccionado);
                        } else {
                            Log.d("API_DEBUG", "Grado no disponible en el primer tema");
                        }

                    } else {
                        Log.d("API_DEBUG", "No hay temas para este idRama");
                    }

                } else {
                    Toast.makeText(RamasTemasActivity.this, "Error al obtener temas", Toast.LENGTH_SHORT).show();
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

        View view = getLayoutInflater().inflate(R.layout.dialog_nueva_rama, null);
        final EditText input = view.findViewById(R.id.edtNombreRama);

        builder.setView(view);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = input.getText().toString().trim();
            if (!nombre.isEmpty()) {
                // Aquí usas el DTO limpio
                RamaCursoCrearDTO nueva = new RamaCursoCrearDTO(cursoId, usuarioId, nombre);
                Log.d("API_DEBUG", "Enviando: " + nueva.toString());

                apiService.crearRama(nueva).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            cargarRamas();
                            Toast.makeText(RamasTemasActivity.this, "Rama agregada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RamasTemasActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            Log.e("API_DEBUG", "Respuesta error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(RamasTemasActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                        Log.e("API_DEBUG", "Error de red: " + t.getMessage(), t);
                    }
                });
            } else {
                Toast.makeText(RamasTemasActivity.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void onEditarRama(RamaCurso rama, int idUsuario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Rama");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText input = new EditText(this);
        input.setText(rama.getNombreRama());
        input.setSelection(input.getText().length());
        input.setHint("Nombre de la rama");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setTextColor(Color.BLACK);
        layout.addView(input);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoNombre = input.getText().toString().trim();

            if (!nuevoNombre.isEmpty()) {
                RamaEditarDto dto = new RamaEditarDto(
                        cursoId,
                        nuevoNombre
                );

                String json = new Gson().toJson(dto);
                Log.d("JSON enviado", json);

                apiService.editarRama(rama.getIdRama(), idUsuario, dto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            cargarRamas();
                            Toast.makeText(RamasTemasActivity.this, "Rama actualizada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RamasTemasActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                            Log.e("API", "Código error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(RamasTemasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                        Log.e("API", "Fallo: " + t.getMessage());
                    }
                });

            } else {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void onEliminarRama(RamaCurso rama, int idUsuario) {
        apiService.eliminarRama(rama.getIdRama(),idUsuario).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RamasTemasActivity.this, "Rama eliminada", Toast.LENGTH_SHORT).show();
                    cargarRamas();
                } else {
                    Toast.makeText(RamasTemasActivity.this, "Error al eliminar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RamasTemasActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void verHistorialRama(RamaCurso rama, int idUsuario) {
        apiService.getHistorialRama(rama.getIdRama()).enqueue(new Callback<List<HistorialRama>>() {
            @Override
            public void onResponse(Call<List<HistorialRama>> call, Response<List<HistorialRama>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<HistorialRama> historial = response.body();
                    mostrarDialogoHistorial(historial);
                } else {
                    Toast.makeText(RamasTemasActivity.this, "No se pudo obtener el historial", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HistorialRama>> call, Throwable t) {
                Toast.makeText(RamasTemasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mostrarDialogoHistorial(List<HistorialRama> historialRama) {
        Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Historial de la Rama");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_historial_rama, null);
        LinearLayout layouRama = view.findViewById(R.id.layoutRama);

        if (historialRama != null && !historialRama.isEmpty()) {
            for (HistorialRama historial : historialRama) {
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
                TextView tvNombreAnterior = crearText("Nombre anterior: ", historial.getNombreAnterior(), false);
                TextView tvNombreNuevo = crearText("Nombre nuevo: ", historial.getNombreNuevo(), false);
                TextView tvFecha = crearText("Fecha: ", historial.getFechaCambio(), false);
                TextView tvUsuario = crearText("Usuario responsable: ", historial.getNombreUsuario(), false);

                container.addView(tvAccion);
                container.addView(tvNombreAnterior);
                container.addView(tvNombreNuevo);
                container.addView(tvFecha);
                container.addView(tvUsuario);

                card.addView(container);
                layouRama.addView(card);
            }
        } else {
            TextView tv = new TextView(context);
            tv.setText("No hay historial para este curso.");
            tv.setPadding(0, 20, 0, 20);
            tv.setTextSize(16);
            tv.setTextColor(Color.GRAY);
            layouRama.addView(tv);
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

    private void mostrarDialogoCrearTema() {
        if (ramaSeleccionada == null) {
            Toast.makeText(this, "Debes seleccionar una rama primero", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Tema");

        View view = getLayoutInflater().inflate(R.layout.dialog_nuevo_tema, null);
        final EditText input = view.findViewById(R.id.edtNombreTema);

        builder.setView(view);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = input.getText().toString().trim();
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            int idGrado = gradoSeleccionado;

            // Construimos el DTO
            TemaCrearDTO temaDTO = new TemaCrearDTO(nombre, ramaSeleccionada.getIdRama(), idGrado);
            Log.d("API_DEBUG", "Enviando: " + new Gson().toJson(temaDTO));

            apiService.crearTema(usuarioId, temaDTO).enqueue(new Callback<TemaCurso>() {
                @Override
                public void onResponse(Call<TemaCurso> call, Response<TemaCurso> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        TemaCurso creado = response.body();

                        listaTemas.add(creado);
                        temasAdapter.notifyItemInserted(listaTemas.size() - 1);

                        Toast.makeText(RamasTemasActivity.this, "Tema agregado correctamente", Toast.LENGTH_SHORT).show();
                        Log.d("API_DEBUG", "Tema creado: " + new Gson().toJson(creado));
                    } else {
                        Toast.makeText(RamasTemasActivity.this, "Error al guardar tema", Toast.LENGTH_SHORT).show();
                        Log.e("API_DEBUG", "Respuesta error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<TemaCurso> call, Throwable t) {
                    Toast.makeText(RamasTemasActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API_DEBUG", "Error de red: " + t.getMessage(), t);
                }
            });
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void EliminarTema(TemaCurso tema, int idUsuario) {
        apiService.eliminarTema(tema.getIdTema(),idUsuario).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RamasTemasActivity.this, "Tema eliminado", Toast.LENGTH_SHORT).show();
                    listaTemas.add(tema);
                } else {
                    Toast.makeText(RamasTemasActivity.this, "Error al eliminar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RamasTemasActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void EditarTema(TemaCurso tema, int idUsuario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Tema");

        // Crear un contenedor para agregar padding
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int paddingPx = (int) (16 * getResources().getDisplayMetrics().density); // 16dp
        layout.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);

        final EditText input = new EditText(this);
        input.setText(tema.getNombre());
        input.setHint("Nombre del tema");
        input.setTextColor(Color.BLACK);
        input.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoNombre = input.getText().toString().trim();
            if(!nuevoNombre.isEmpty()){
                TemaEditarDTO temadto = new TemaEditarDTO(
                        tema.getIdTema(),
                        nuevoNombre,
                        idUsuario,
                        tema.getIdRama()
                );
                String json = new Gson().toJson(temadto);
                Log.d("JSON enviado", json);

                apiService.editarTema(tema.getIdTema(),idUsuario,temadto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(RamasTemasActivity.this, "Tema actualizado", Toast.LENGTH_SHORT).show();
                            mostrarTemasDeRama(ramaSeleccionada.getIdRama());
                        } else {
                            Toast.makeText(RamasTemasActivity.this, "Error al actualizar tema", Toast.LENGTH_SHORT).show();
                            Log.e("API", "Código error: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(RamasTemasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

}