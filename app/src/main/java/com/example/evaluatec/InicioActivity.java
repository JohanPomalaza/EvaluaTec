package com.example.evaluatec;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InicioActivity extends AppCompatActivity {

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);

        logo = findViewById(R.id.logoImage);
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.logo_slide_in);
        logo.startAnimation(slideIn);

        // Añadir animación con ObjectAnimator (entrada desde arriba)
        ObjectAnimator animator = ObjectAnimator.ofFloat(logo, "translationY", -200f, 0f);
        animator.setDuration(1200);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();

        // Esperar 3 segundos y redirigir a la pantalla de selección de usuario
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(InicioActivity.this, TipoUsuarioActivity.class));
            finish();
        }, 3000);
    }
}