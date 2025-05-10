package com.example.evaluatec.pantallas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.evaluatec.R;

public class CoursesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        // Configurar botón de retroceso en la barra de acción
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Cierra esta actividad y vuelve a MainActivity
        return true;
    }
}
