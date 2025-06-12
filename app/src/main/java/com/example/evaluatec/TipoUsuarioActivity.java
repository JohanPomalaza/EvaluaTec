package com.example.evaluatec;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.evaluatec.login.LoginActivity;

public class TipoUsuarioActivity extends AppCompatActivity {


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tipo_usuario);


        findViewById(R.id.btnEstudiante).setOnClickListener(v -> abrirLogin("ESTUDIANTE"));
        findViewById(R.id.btnProfesor).setOnClickListener(v -> abrirLogin("PROFESOR"));
        findViewById(R.id.btnAdmin).setOnClickListener(v -> abrirLogin("ENTIDAD"));

    }
    private void abrirLogin(String tipoUsuario) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("tipoUsuario", tipoUsuario);
        startActivity(intent);
    }

}