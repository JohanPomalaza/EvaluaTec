package com.example.evaluatec.pantallas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.ResumenDocenteDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfesorActivity extends AppCompatActivity {
    private int usuarioId;
    private TextView tvWelcome, tvDocenteNombre, tvCantidadAlumnos, tvCantidadCursos;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_profesor_activity);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvDocenteNombre = findViewById(R.id.tvDocenteNombre);
        tvCantidadAlumnos = findViewById(R.id.tvCantidadAlumnos);
        tvCantidadCursos = findViewById(R.id.tvCantidadCursos);
        apiService = ApiCliente.getClient().create(ApiService.class);

        obtenerDatosUsuario();
        cargarResumen();

        usuarioId = getIntent().getIntExtra("usuarioId", -1);
        if (usuarioId == -1) {
            finish();
            return;
        }

        CardView btnSecciones = findViewById(R.id.btnSecciones);
        btnSecciones.setOnClickListener(v -> {
            Intent intent = new Intent(ProfesorActivity.this, SeccionesActivity.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
        });

    }

    private void obtenerDatosUsuario() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuarioId = extras.getInt("usuarioId", 0);
            String nombre = extras != null ? extras.getString("nombre", "Usuario") : "Usuario";
            String apellido = extras != null ? extras.getString("apellido", "") : "";
            tvDocenteNombre.setText("Docente " + nombre + " " + apellido);
        } else {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void cargarResumen() {
        apiService.obtenerResumenDocente(usuarioId).enqueue(new Callback<ResumenDocenteDto>() {
            @Override
            public void onResponse(Call<ResumenDocenteDto> call, Response<ResumenDocenteDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResumenDocenteDto resumen = response.body();
                    tvCantidadCursos.setText(String.valueOf(resumen.getCursosAsignados()));
                    tvCantidadAlumnos.setText(String.valueOf(resumen.getAlumnosAsignados()));
                } else {
                    Toast.makeText(ProfesorActivity.this, "No se pudo cargar el resumen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResumenDocenteDto> call, Throwable t) {
                Toast.makeText(ProfesorActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

}