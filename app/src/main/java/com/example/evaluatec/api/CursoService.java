package com.example.evaluatec.api;

import com.example.evaluatec.modelos.Curso;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CursoService {
    @GET("api/curso") // Ajusta esta ruta según tu API
    Call<List<Curso>> getCursos(@Header("Authorization") String authToken);
}
