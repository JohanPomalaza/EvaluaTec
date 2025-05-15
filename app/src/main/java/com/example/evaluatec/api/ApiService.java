package com.example.evaluatec.api;
import com.example.evaluatec.modelos.Curso;
import com.example.evaluatec.modelos.Estudiante;
import com.example.evaluatec.modelos.Nota;
import com.example.evaluatec.modelos.NotaEstudiante;
import com.example.evaluatec.modelos.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/login/login")
    Call<LoginResponse> login(@Body Usuario usuario);

    @GET("api/Curso/usuario/{idUsuario}/cursos")
    Call<List<Curso>> getCursosPorUsuario(@Path("idUsuario") int idUsuario);

    @GET("/api/Curso/usuario/{idUsuario}/curso/{idCurso}/notas")
    Call<List<Nota>> getNotasPorCurso(
            @Path("idUsuario") int idUsuario,
            @Path("idCurso") int idCurso);

    @GET("api/Curso/docente/{id_usuario}/cursos")
    Call<List<Curso>> getCursosPorDocente(@Path("id_usuario") int id_usuario);

    @GET("api/Curso/docente/{id_usuario}/curso/{id_curso}/estudiantes")
    Call<List<Estudiante>> getEstudiantesPorCurso(@Path("id_usuario") int usuarioId, @Path("id_curso") int cursoId);

    @GET("api/Curso/docente/{id_usuario}/curso/{id_curso}/notas")
    Call<List<NotaEstudiante>> getNotasPorEstudiante(@Path("id_usuario") int usuarioId, @Path("id_curso") int cursoId);


    @POST("api/Curso/notas")
    Call<Void> agregarOEditarNota(
            @Query("id_usuario_estudiante") int idUsuarioEstudiante,
            @Query("id_tema") int idTema,
            @Query("nota") float nota
    );
}
