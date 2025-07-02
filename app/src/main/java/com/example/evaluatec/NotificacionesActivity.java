package com.example.evaluatec;

import android.os.Bundle;

import com.example.evaluatec.adaptadores.NotificacionAdapter;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.NotificacionDto;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.databinding.ActivityNotificacionesBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacionesActivity extends AppCompatActivity {

    private RecyclerView rvNotificaciones;
    private NotificacionAdapter adapter;
    private List<NotificacionDto> listaNotificaciones = new ArrayList<>();
    private ApiService apiService;
    private int usuarioId;
    private TextView badgeNotificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        rvNotificaciones = findViewById(R.id.rvNotificaciones);

        apiService = ApiCliente.getClient().create(ApiService.class);
        listaNotificaciones = new ArrayList<>();

        adapter = new NotificacionAdapter(this, listaNotificaciones, apiService, this::actualizarBadge);
        rvNotificaciones.setLayoutManager(new LinearLayoutManager(this));
        rvNotificaciones.setAdapter(adapter);

        usuarioId = getIntent().getIntExtra("usuarioId", 0);
        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        apiService.getNotificacionesNoLeidas(usuarioId).enqueue(new Callback<List<NotificacionDto>>() {
            @Override
            public void onResponse(Call<List<NotificacionDto>> call, Response<List<NotificacionDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaNotificaciones.clear();
                    listaNotificaciones.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    actualizarBadge();
                } else {
                    Toast.makeText(NotificacionesActivity.this, "Error al cargar notificaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotificacionDto>> call, Throwable t) {
                Toast.makeText(NotificacionesActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void actualizarBadge() {
        // solo si existe el TextView
        if (badgeNotificaciones != null) {
            runOnUiThread(() -> badgeNotificaciones.setText("0"));
        }
    }
}