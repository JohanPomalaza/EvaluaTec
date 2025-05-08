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


public class perfil_activity extends AppCompatActivity {

    private Button btnIngresarNotas;
    private Button btnEditarContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profesor_activity);

        btnIngresarNotas = findViewById(R.id.btnIngresarNotas);
        btnEditarContenido = findViewById(R.id.btnEditarContenido);

        Toast.makeText(this, "Bienvenido, Profesor", Toast.LENGTH_SHORT).show();

        btnIngresarNotas.setOnClickListener(v -> {
            // Lógica para ingresar notas
            Toast.makeText(this, "Ingresar Notas", Toast.LENGTH_SHORT).show();
        });

        btnEditarContenido.setOnClickListener(v -> {
            // Lógica para editar contenido
            Toast.makeText(this, "Editar Contenido", Toast.LENGTH_SHORT).show();
        });
    }
}