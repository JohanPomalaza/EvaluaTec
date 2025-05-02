package com.example.evaluatec.pantallas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.evaluatec.R;


public class EstudianteActivity extends AppCompatActivity {

    private Button btnVerNotas;
    private Button btnVerContenidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_estudiante_activity);

        btnVerNotas = findViewById(R.id.btnVerNotas);
        btnVerContenidos = findViewById(R.id.btnVerContenidos);

        int usuarioId = getIntent().getIntExtra("usuarioId", -1);
        String rol = getIntent().getStringExtra("rol");

        Toast.makeText(this, "Bienvenido, Estudiante", Toast.LENGTH_SHORT).show();

        btnVerNotas.setOnClickListener(v -> {
            // Lógica para ver notas
            Toast.makeText(this, "Ver Notas", Toast.LENGTH_SHORT).show();
        });

        btnVerContenidos.setOnClickListener(v -> {
            // Lógica para ver contenido
            Toast.makeText(this, "Ver Contenidos", Toast.LENGTH_SHORT).show();
        });
    }
}