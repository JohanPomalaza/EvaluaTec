package com.example.evaluatec.pantallas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.evaluatec.R;

public class GradesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        // Configurar botón de retroceso en la ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Cierra la actividad y vuelve a MainActivity
        return true;
    }
}
