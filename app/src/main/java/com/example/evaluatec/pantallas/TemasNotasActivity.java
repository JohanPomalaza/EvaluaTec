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

    private RecyclerView recyclerTemas;
    private NotaAdapterDocente temasAdapter;
    private int idRama, alumnoId, anioEscolar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas_notas);

        recyclerTemas = findViewById(R.id.recyclerViewNotas);
        recyclerTemas.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiCliente.getClient().create(ApiService.class);

        idRama = getIntent().getIntExtra("idRama", -1);
        alumnoId = getIntent().getIntExtra("alumnoId", -1);
        anioEscolar = getIntent().getIntExtra("anioEscolar", -1);


        cargarTemas();
    }
    private void cargarTemas() {
        apiService.getNotasPorCurso(alumnoId, idRama, anioEscolar).enqueue(new Callback<List<Nota>>() {
            @Override
            public void onResponse(Call<List<Nota>> call, Response<List<Nota>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Nota> notas = response.body();
                    temasAdapter = new NotaAdapterDocente(TemasNotasActivity.this, notas, alumnoId);
                    recyclerTemas.setAdapter(temasAdapter);
                } else {
                    Toast.makeText(TemasNotasActivity.this, "No hay temas para este curso", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Nota>> call, Throwable t) {
                Toast.makeText(TemasNotasActivity.this, "Error al cargar temas: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}