package com.example.evaluatec.pantallas;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.Nota;
import com.example.evaluatec.modelos.NotaEstudiante;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemasNotasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotaAdapterDocente notaAdapterDocente;
    private List<Nota> listaNotas = new ArrayList<>();
    private ApiService apiService;

    private int idUsuarioEstudiante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas_notas);

        recyclerView = findViewById(R.id.recyclerViewNotas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiCliente.getClient().create(ApiService.class);

        int usuarioId = getIntent().getIntExtra("usuarioId", 0);
        int cursoId = getIntent().getIntExtra("cursoId", 0);
        idUsuarioEstudiante = getIntent().getIntExtra("estudianteId", 0);

        notaAdapterDocente = new NotaAdapterDocente(listaNotas, idUsuarioEstudiante, this::mostrarDialogoEditarNota);
        recyclerView.setAdapter(notaAdapterDocente);

        cargarNotas(usuarioId, cursoId, idUsuarioEstudiante);
    }

    private void mostrarDialogoEditarNota(Nota nota, int idUsuarioEstudiante) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Nota");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(String.valueOf(nota.getNota()));
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            double nuevaNota = Double.parseDouble(input.getText().toString());
            nota.setNota(nuevaNota);
            notaAdapterDocente.notifyDataSetChanged();

            guardarNotaEnAPI(idUsuarioEstudiante, nota.getIdTema(), (float) nuevaNota);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void guardarNotaEnAPI(int idUsuarioEstudiante, int idTema, float nuevaNota) {
        apiService.agregarOEditarNota(idUsuarioEstudiante, idTema, nuevaNota)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(TemasNotasActivity.this, "Nota guardada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TemasNotasActivity.this, "Error al guardar nota: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(TemasNotasActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cargarNotas(int usuarioId, int cursoId, int idUsuarioEstudiante) {
        apiService.getNotasPorEstudiante(usuarioId, cursoId).enqueue(new Callback<List<NotaEstudiante>>() {
            @Override
            public void onResponse(Call<List<NotaEstudiante>> call, Response<List<NotaEstudiante>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaNotas.clear();
                    listaNotas.addAll(response.body().get(0).getNotas());
                    notaAdapterDocente.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<NotaEstudiante>> call, Throwable t) {
                Toast.makeText(TemasNotasActivity.this, "Error al cargar notas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}