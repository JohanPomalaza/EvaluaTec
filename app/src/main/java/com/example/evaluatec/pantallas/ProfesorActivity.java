package com.example.evaluatec.pantallas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.evaluatec.R;

public class ProfesorActivity extends AppCompatActivity {
    private int usuarioId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_profesor_activity);

        usuarioId = getIntent().getIntExtra("usuarioId", -1);
        if (usuarioId == -1) {
            finish();
            return;
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnSecciones = findViewById(R.id.btnViewSecciones);
        btnSecciones.setOnClickListener(v -> {
            Intent intent = new Intent(ProfesorActivity.this, SeccionesActivity.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
        });


        /*Button btnCourses = findViewById(R.id.btnViewCourses);
        btnCourses.setOnClickListener(v -> {
            Intent intent = new Intent(ProfesorActivity.this, fragment_cursos_docente.class);
            intent.putExtra("usuarioId", usuarioId);
            startActivity(intent);
        });*/

        // Configurar botón de registrar notas
        Button btnRegisterGrades = findViewById(R.id.btnRegisterGrades);
        btnRegisterGrades.setOnClickListener(v -> {
            Intent intent = new Intent(ProfesorActivity.this, GradesActivity.class);
            startActivity(intent);
        });
    }

}