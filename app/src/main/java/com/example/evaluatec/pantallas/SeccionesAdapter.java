package com.example.evaluatec.pantallas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluatec.R;
import com.example.evaluatec.api.ApiCliente;
import com.example.evaluatec.api.ApiService;
import com.example.evaluatec.modelos.Alumno;
import com.example.evaluatec.modelos.Secciones;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeccionesAdapter extends RecyclerView.Adapter<SeccionesAdapter.ViewHolder>{
    private List<Secciones> listaSecciones;
    private ApiService apiService;

    private Context context;

    public SeccionesAdapter(Context context, List<Secciones> listaSecciones) {
        this.listaSecciones = listaSecciones;
        this.apiService = ApiCliente.getClient().create(ApiService.class);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seccion, parent, false);
        return new ViewHolder(vista);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Secciones seccion = listaSecciones.get(position);
        holder.txtTitulo.setText(seccion.getNombreGrado());

        holder.txtTitulo.setOnClickListener(v -> {
            if (holder.layoutAlumnos.getVisibility() == View.GONE) {
                holder.layoutAlumnos.setVisibility(View.VISIBLE);
                holder.layoutAlumnos.removeAllViews();

                int idGrado = seccion.getIdGrado();
                int idAnio = seccion.getIdAnioEscolar();

            apiService.getAlumnosPorGrado(idGrado, idAnio).enqueue(new Callback<List<Alumno>>() {
                @Override
                public void onResponse(Call<List<Alumno>> call, Response<List<Alumno>> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        for (Alumno alumno : response.body()) {
                            // Contenedor principal para el alumno
                            CardView card = new CardView(holder.layoutAlumnos.getContext());
                            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            cardParams.setMargins(0, 8, 0, 8);
                            card.setLayoutParams(cardParams);
                            card.setRadius(12f);
                            card.setCardElevation(6f);
                            card.setUseCompatPadding(true);
                            card.setClickable(true);
                            card.setForeground(ContextCompat.getDrawable(holder.layoutAlumnos.getContext(), R.drawable.selectable_item_background)); // para ripple touch effect

                            // Layout horizontal dentro del CardView
                            LinearLayout container = new LinearLayout(holder.layoutAlumnos.getContext());
                            container.setOrientation(LinearLayout.HORIZONTAL);
                            container.setPadding(16, 16, 16, 16);
                            container.setGravity(Gravity.CENTER_VERTICAL);

                            // Icono avatar (puede ser imagen o icono predeterminado)
                            ImageView avatar = new ImageView(holder.layoutAlumnos.getContext());
                            LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(64, 64);
                            avatarParams.setMargins(0, 0, 16, 0);
                            avatar.setLayoutParams(avatarParams);
                            avatar.setImageResource(R.drawable.ic_user);  // Ícono genérico de persona
                            avatar.setColorFilter(ContextCompat.getColor(holder.layoutAlumnos.getContext(), R.color.primary)); // color acorde a tu app

                            // TextView con el nombre
                            TextView tvNombre = new TextView(holder.layoutAlumnos.getContext());
                            tvNombre.setText(alumno.getNombreCompleto());
                            tvNombre.setTextSize(16);
                            tvNombre.setTypeface(null, Typeface.BOLD);
                            tvNombre.setTextColor(ContextCompat.getColor(holder.layoutAlumnos.getContext(), R.color.text_primary));
                            tvNombre.setMaxLines(1);
                            tvNombre.setEllipsize(TextUtils.TruncateAt.END);

                            // Añadir views al layout
                            container.addView(avatar);
                            container.addView(tvNombre);

                            // Añadir layout al CardView
                            card.addView(container);

                            // Acción al hacer clic en el CardView completo
                            card.setOnClickListener(v -> {
                                Intent intent = new Intent(holder.layoutAlumnos.getContext(), CursosAlumnoActivity.class);
                                intent.putExtra("alumnoId", alumno.getIdUsuarioEstudiante());
                                intent.putExtra("anioEscolar", idAnio);
                                holder.layoutAlumnos.getContext().startActivity(intent);
                            });

                            // Finalmente, agregar el CardView a layoutAlumnos
                            holder.layoutAlumnos.addView(card);
                        }
                    }else{
                        TextView errorTv = new TextView(holder.layoutAlumnos.getContext());
                        errorTv.setText("No hay alumnos.");
                        holder.layoutAlumnos.addView(errorTv);
                    }
                }

                @Override
                public void onFailure(Call<List<Alumno>> call, Throwable t) {
                    TextView errorTv = new TextView(holder.layoutAlumnos.getContext());
                    errorTv.setText("Error al cargar alumnos.");
                    holder.layoutAlumnos.addView(errorTv);
                }
            });
            } else {
                holder.layoutAlumnos.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public int getItemCount() {
        return listaSecciones.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo;
        LinearLayout layoutAlumnos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloSeccion);
            layoutAlumnos = itemView.findViewById(R.id.layoutAlumnos);
        }
    }
}
