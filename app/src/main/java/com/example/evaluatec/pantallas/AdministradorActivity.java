package com.example.evaluatec.pantallas;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.evaluatec.R;
import com.google.android.material.card.MaterialCardView;

public class AdministradorActivity extends AppCompatActivity {
    private MotionLayout motionLayout;
    private MaterialCardView cardCreateCourse, cardProfessors, cardStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        motionLayout = findViewById(R.id.motionLayout);
        cardCreateCourse = findViewById(R.id.cardCreateCourse);
        cardProfessors = findViewById(R.id.cardProfessors);
        cardStudents = findViewById(R.id.cardStudents);

        // Inicia la animación automáticamente al cargar la pantalla
        motionLayout.transitionToEnd();


        // Opcional: listeners para cambios de estado de animación
        cardCreateCourse.setOnClickListener(v -> {
            Intent intent = new Intent(AdministradorActivity.this, CursoMantenimientoActivity.class);
            startActivity(intent);
        });

        cardProfessors.setOnClickListener(v -> {
            Intent intent = new Intent(AdministradorActivity.this, ProfesoresMantenimientoActivity.class);
            startActivity(intent);
        });

        cardStudents.setOnClickListener(v -> {
            Intent intent = new Intent(AdministradorActivity.this, EstudiantesMantenimientoActivity.class);
            startActivity(intent);
        });
    }
}