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

public class ProfesorActivity extends AppCompatActivity {
    private int usuarioId;
    private TextView tvWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_profesor_activity);
        tvWelcome = findViewById(R.id.tvWelcome);
        obtenerDatosUsuario();

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



        // Configurar botón de registrar notas
        CardView  btnRegisterGrades = findViewById(R.id.btnRegistrarNotas);
        btnRegisterGrades.setOnClickListener(v -> {
            Intent intent = new Intent(ProfesorActivity.this, GradesActivity.class);
            startActivity(intent);
        });
    }

    private void obtenerDatosUsuario() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuarioId = extras.getInt("usuarioId", 0);
            String nombre = extras != null ? extras.getString("nombre", "Usuario") : "Usuario";
            String apellido = extras != null ? extras.getString("apellido", "") : "";
            tvWelcome.setText("Bienvenido " + nombre + " " + apellido);
        } else {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}