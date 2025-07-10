package com.example.evaluatec.pantallas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.adaptadores.ProfesoresAdapter;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.AsignacionActualizarDto;
import com.example.evaluatec.modelos.AsignacionCrearDto;
import com.example.evaluatec.modelos.AsignacionDto;
import com.example.evaluatec.modelos.DocenteCrearDto;
import com.example.evaluatec.modelos.DocenteDto;
import com.example.evaluatec.modelos.DocenteEditarDto;
import com.example.evaluatec.modelos.GradoDto;
import com.example.evaluatec.modelos.RamaDto;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfesoresMantenimientoActivity extends AppCompatActivity  {
    private RecyclerView recyclerProfesores;
    private ProfesoresAdapter adapter;
    private FloatingActionButton fabAgregar;
    private ApiService apiService;

    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesores_mantenimiento);

        recyclerProfesores = findViewById(R.id.recyclerProfesores);
        recyclerProfesores.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiCliente.getClient().create(ApiService.class);
        usuarioId = getIntent().getIntExtra("usuarioId", 0);
        fabAgregar = findViewById(R.id.fabAgregar);

        fabAgregar.setOnClickListener(v -> mostrarDialogoProfesor(null));

        cargarDocentes();

    }
    private void cargarDocentes() {
        apiService.getDocentes().enqueue(new Callback<List<DocenteDto>>() {
            @Override
            public void onResponse(Call<List<DocenteDto>> call, Response<List<DocenteDto>> response) {
                if (response.isSuccessful()) {
                    List<DocenteDto> lista = response.body();
                    adapter = new ProfesoresAdapter(lista, usuarioId, new ProfesoresAdapter.OnProfesorActionListener() {
                        @Override
                        public void onEditar(DocenteDto docente) {
                            mostrarDialogoProfesor(docente);
                        }
                        @Override
                        public void onEliminar(DocenteDto docente) {
                            mostrarDialogoConfirmacionEliminar(docente.idUsuario);
                        }

                        @Override
                        public void onVerAsignaciones(DocenteDto docente) {
                            Intent intent = new Intent(ProfesoresMantenimientoActivity.this, activity_asignaciones.class);
                            intent.putExtra("idDocente", docente.idUsuario);
                            intent.putExtra("nombreDocente", docente.nombre + " " + docente.apellido);
                            startActivity(intent);
                        }
                    });
                    recyclerProfesores.setAdapter(adapter);
                } else {
                    Toast.makeText(ProfesoresMantenimientoActivity.this, "Error al cargar docentes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DocenteDto>> call, Throwable t) {
                Toast.makeText(ProfesoresMantenimientoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoProfesor(DocenteDto docenteExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(docenteExistente == null ? "Nuevo Docente" : "Editar Docente");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_profesor, null);
        builder.setView(view);

        TextInputEditText edtNombre = view.findViewById(R.id.edtNombre);
        TextInputEditText edtApellido = view.findViewById(R.id.edtApellido);
        TextInputEditText edtCorreo = view.findViewById(R.id.edtCorreo);
        TextInputEditText edtContrasena = view.findViewById(R.id.edtContrasena);

        if (docenteExistente != null) {
            edtNombre.setText(docenteExistente.nombre);
            edtApellido.setText(docenteExistente.apellido);
            edtCorreo.setText(docenteExistente.correo);
        }

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = edtNombre.getText().toString().trim();
            String apellido = edtApellido.getText().toString().trim();
            String correo = edtCorreo.getText().toString().trim();
            String contrasena = edtContrasena.getText().toString().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty()|| contrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (docenteExistente == null) {
                DocenteCrearDto nuevo = new DocenteCrearDto();
                nuevo.nombre = nombre;
                nuevo.apellido = apellido;
                nuevo.correo = correo;
                nuevo.contrasena = contrasena;
                crearDocente(nuevo);
            } else {
                DocenteEditarDto actualizado = new DocenteEditarDto(nombre,apellido,correo,contrasena);
                editarDocente(docenteExistente.idUsuario, actualizado);
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void crearDocente(DocenteCrearDto dto) {
        Gson gson = new Gson();
        String json = gson.toJson(dto);
        Log.d("CREAR_DOCENTE_JSON", json);
        apiService.crearDocente(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfesoresMantenimientoActivity.this, "Docente creado", Toast.LENGTH_SHORT).show();
                    cargarDocentes();
                } else {
                    Toast.makeText(ProfesoresMantenimientoActivity.this, "Error al crear y/o el correo ingresado ya esta registrado", Toast.LENGTH_SHORT).show();
                    Log.e("CREAR_DOCENTE_ERROR", "Código: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProfesoresMantenimientoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CREAR_DOCENTE_FAILURE", t.getMessage(), t);
            }
        });
    }

    private void editarDocente(int id, DocenteEditarDto dto) {
        apiService.editarDocente(id, dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfesoresMantenimientoActivity.this, "Docente actualizado", Toast.LENGTH_SHORT).show();
                    cargarDocentes();
                } else {
                    Toast.makeText(ProfesoresMantenimientoActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProfesoresMantenimientoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mostrarDialogoConfirmacionEliminar(int idDocente) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar")
                .setMessage("¿Deseas inactivar este docente?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    apiService.inactivarDocente(idDocente).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Docente inactivado", Toast.LENGTH_SHORT).show();
                                cargarDocentes(); // método para refrescar la lista
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
}