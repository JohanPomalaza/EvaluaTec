package com.example.evaluatec.pantallas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evaluatec.R;


public class perfil_activity extends AppCompatActivity {

    private int usuarioId;
    private TextView txtNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_perfil_activity);
        Bundle extras = getIntent().getExtras();
        txtNombre = findViewById(R.id.txtNombre);

        if (extras != null) {
            usuarioId = extras.getInt("usuarioId", 0);
            String nombre = extras != null ? extras.getString("nombre", "Usuario") : "Usuario";
            String apellido = extras != null ? extras.getString("apellido", "") : "";
            txtNombre.setText(nombre + " " + apellido);
        } else {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}