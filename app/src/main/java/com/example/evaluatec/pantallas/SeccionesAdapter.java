package com.example.evaluatec.pantallas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
                            TextView tv = new TextView(holder.layoutAlumnos.getContext());
                            tv.setText(alumno.getNombreCompleto());
                            tv.setPadding(16, 8, 16, 8);
                            tv.setTextSize(16);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, 8, 0, 8); // Margen entre alumnos
                            tv.setLayoutParams(params);
                            tv.setBackground(ContextCompat.getDrawable(holder.layoutAlumnos.getContext(), R.drawable.bg_alumno_item));
                            tv.setTextColor(Color.BLACK);
                            tv.setOnClickListener(v -> {
                                Intent intent = new Intent(holder.layoutAlumnos.getContext(), CursosAlumnoActivity.class);
                                intent.putExtra("alumnoId", alumno.getIdUsuarioEstudiante());
                                intent.putExtra("anioEscolar", idAnio);
                                context.startActivity(intent);
                            });
                            holder.layoutAlumnos.addView(tv);
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
