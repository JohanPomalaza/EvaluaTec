package com.example.evaluatec.api;

import com.example.evaluatec.modelos.Curso;
import com.example.evaluatec.modelos.Nota;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface CursoService {
    @GET("api/Curso/usuario/{idUsuario}/cursos")
    Call<List<Curso>> getCursosPorUsuario(@Path("idUsuario") int idUsuario);
}
