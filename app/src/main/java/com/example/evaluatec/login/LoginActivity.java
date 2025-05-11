package com.example.evaluatec.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.api.LoginResponse;
import com.example.evaluatec.modelos.Usuario;
import com.example.evaluatec.pantallas.EstudianteActivity;
import com.example.evaluatec.pantallas.ProfesorActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
//aqui se crean las variables a usar para el login, junto con la variable para la conexion
    private EditText usernameField;
    private EditText passwordField;
    private ImageButton loginButton;
    private Button registerButton;
    private ApiService apiService; // Retrofit service

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //se enlaza las vistas con las variables
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        constraintSet.setVerticalBias(R.id.titleLogin, 0.4f);
        constraintSet.applyTo(layout);
        String baseUrl = "http://10.0.2.2:5262/";
        // Inicializa Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) // Accede a tu API desde el emulador
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        loginButton.setOnClickListener(view -> handleLogin());
        registerButton.setOnClickListener(view -> handleRegister());
    }

    private void handleLogin() {
        String correo = usernameField.getText().toString();
        String contrasena = passwordField.getText().toString();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario(correo, contrasena);

        Call<LoginResponse> call = apiService.login(usuario);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse login = response.body();
                    String rol = login.getRol();
                    String nombre = login.getNombre();
                    String apellido = login.getApellido();

                    Toast.makeText(LoginActivity.this, "Login exitoso: " + login.getMensaje(), Toast.LENGTH_SHORT).show();

                    //Aqui obtiene la pantalla que corresponde segun rol
                    Intent intent;
                    if (login.getRol().equalsIgnoreCase("PROFESOR")) {
                        intent = new Intent(LoginActivity.this, ProfesorActivity.class);
                    } else {
                        intent = new Intent(LoginActivity.this, EstudianteActivity.class);
                        intent.putExtra("nombre", nombre);
                        intent.putExtra("apellido", apellido);
                    }
                    //Se pasa el id del rol
                    intent.putExtra("usuarioId", login.getId());
                    intent.putExtra("rol", login.getRol());

                    startActivity(intent);
                    finish(); // Cierra la actividad actual
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleRegister() {
        Toast.makeText(this, "Go to Register Screen", Toast.LENGTH_SHORT).show();
        //Esto seria para la activida de registro pero creo que eso se manejaria de forma interna
    }
}