package com.example.evaluatec.login;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.example.evaluatec.pantallas.AdministradorActivity;
import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.api.LoginResponse;
import com.example.evaluatec.modelos.Usuario;
import com.example.evaluatec.pantallas.EstudianteActivity;
import com.example.evaluatec.pantallas.ProfesorActivity;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
//aqui se crean las variables a usar para el login, junto con la variable para la conexion
    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private ApiService apiService;

    String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //se enlaza las vistas con las variables
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);

        tipoUsuario = getIntent().getStringExtra("tipoUsuario");

        if(tipoUsuario == null){
            tipoUsuario = "ESTUDIANTE";
        }


        TextView title = findViewById(R.id.titleLogin);
        TextInputLayout usernameLayout = findViewById(R.id.username_layout);
        TextInputLayout passwordLayout = findViewById(R.id.password_layout);

        int color;
        switch (tipoUsuario.toUpperCase()) {
            case "PROFESOR":
                color = ContextCompat.getColor(this, R.color.red_dark);
                break;
            case "ADMINISTRADOR":
                color = ContextCompat.getColor(this, R.color.green_dark);
                break;
            default:
                color = ContextCompat.getColor(this, R.color.blue_primary);
                break;
        }

        title.setTextColor(color);
        usernameLayout.setBoxStrokeColor(color);
        passwordLayout.setBoxStrokeColor(color);
        loginButton.setBackgroundTintList(ColorStateList.valueOf(color));
        ((TextView) findViewById(R.id.forgot_password)).setTextColor(color);


        ConstraintLayout layout = findViewById(R.id.constraintLayout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        constraintSet.setVerticalBias(R.id.titleLogin, 0.4f);
        constraintSet.applyTo(layout);

        String baseUrl = "http://10.0.2.2:5262/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5262/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        loginButton.setOnClickListener(v -> handleLogin(tipoUsuario));
    }

    private void handleLogin(String tipoUsuario) {
        String correo = usernameField.getText().toString();
        String contrasena = passwordField.getText().toString();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Debe ingresar el correo", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario(correo, contrasena);

        Call<LoginResponse> call = apiService.login(usuario);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse login = response.body();
                    if (!login.getRol().equalsIgnoreCase(tipoUsuario)) {
                        Toast.makeText(LoginActivity.this, "Rol incorrecto", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String rol = login.getRol();
                    String nombre = login.getNombre();
                    String apellido = login.getApellido();

                    Toast.makeText(LoginActivity.this, "Login exitoso: " + login.getMensaje(), Toast.LENGTH_SHORT).show();

                    //Aqui obtiene la pantalla que corresponde segun rol
                    Intent intent;
                    if (login.getRol().equalsIgnoreCase("PROFESOR")) {
                        intent = new Intent(LoginActivity.this, ProfesorActivity.class);
                        intent.putExtra("nombre", nombre);
                        intent.putExtra("apellido", apellido);
                    } else if (login.getRol().equalsIgnoreCase("ESTUDIANTE")) {
                        intent = new Intent(LoginActivity.this, EstudianteActivity.class);
                        intent.putExtra("nombre", nombre);
                        intent.putExtra("apellido", apellido);
                    }else {
                        intent = new Intent(LoginActivity.this, AdministradorActivity.class);
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