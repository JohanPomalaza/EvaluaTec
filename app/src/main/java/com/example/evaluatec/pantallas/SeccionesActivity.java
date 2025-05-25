package com.example.evaluatec.pantallas;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.Secciones;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeccionesActivity extends AppCompatActivity {

    private RecyclerView recyclerSecciones;
    private SeccionesAdapter seccionesAdapter;
    private ApiService apiService;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_secciones);

        recyclerSecciones = findViewById(R.id.rvSecciones);
        recyclerSecciones.setLayoutManager(new LinearLayoutManager(this));

        usuarioId = getIntent().getIntExtra("usuarioId", -1);
        apiService = ApiCliente.getClient().create(ApiService.class);

        apiService.getSeccionesPorDocente(usuarioId).enqueue(new Callback<List<Secciones>>() {
            @Override
            public void onResponse(Call<List<Secciones>> call, Response<List<Secciones>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    seccionesAdapter = new SeccionesAdapter(SeccionesActivity.this, response.body());
                    recyclerSecciones.setAdapter(seccionesAdapter);
                } else {
                    Toast.makeText(SeccionesActivity.this, "No se encontraron secciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Secciones>> call, Throwable t) {
                Toast.makeText(SeccionesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}