package com.example.evaluatec.api;
import com.example.evaluatec.modelos.Alumno;
import com.example.evaluatec.modelos.Curso;
import com.example.evaluatec.modelos.CursoMantenimiento;
import com.example.evaluatec.modelos.Estudiante;
import com.example.evaluatec.modelos.HistorialNota;
import com.example.evaluatec.modelos.Nota;
import com.example.evaluatec.modelos.NotaPorCurso;
import com.example.evaluatec.modelos.RamaCurso;
import com.example.evaluatec.modelos.Secciones;
import com.example.evaluatec.modelos.TemaCurso;
import com.example.evaluatec.modelos.TemaEditarDTO;
import com.example.evaluatec.modelos.Usuario;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/login/login")
    Call<LoginResponse> login(@Body Usuario usuario);

    /* LISTA LOS CURSOS DEL USUARIO */
    @GET("api/Curso/usuario/{idUsuario}/cursos")
    Call<List<Curso>> getCursosPorUsuario(@Path("idUsuario") int idUsuario);

    @GET("api/Curso/alumno/{idUsuario}/cursos")
    Call<List<Curso>> getCursosPorAlumno(@Path("idUsuario") int idUsuario);
    /* ------------------------------- */

/*LISTA LAS NOTAS SEGUN ESTUDIANTE*/

    @GET("/api/Curso/usuario/{idUsuario}/curso/{idCurso}/notas")
    Call<List<NotaPorCurso>> getNotasPorCurso(
            @Path("idUsuario") int idUsuario,
            @Path("idCurso") int idCurso);

    /* ------------------------------- */

    /*SE LISTA LAS SECCIONES DEL DOCENTE*/
    @GET("api/Curso/docente/{id_docente}/secciones")
    Call<List<Secciones>> getSeccionesPorDocente(@Path("id_docente") int usuarioId);
    /* ------------------------------- */

    /*SE LISTA LOS ALUMNOS DE LA SECCION ASIGNADA AL DOCENTE*/
    @GET("api/Curso/grado/{id_grado}/anio/{id_anio}/alumnos")
    Call<List<Alumno>> getAlumnosPorGrado(
            @Path("id_grado") int idGrado,
            @Path("id_anio") int idAnio
    );
    /* ------------------------------- */

    /*SE LISTA LAS NOTAS DE LOS ALUMNOS DE LA SECCION ASIGNADA AL DOCENTE*/
    @GET("api/Curso/alumno/{idUsuario}/curso/{idRama}/anio/{idAnio}/notas")
    Call<List<Nota>> getNotasPorCurso(
            @Path("idUsuario") int idUsuario,
            @Path("idRama") int idRama,
            @Path("idAnio") int idAnio
    );
    /* ------------------------------- */
    /*REGISTRO DE NOTAS Y COMENTARIOS*/
    @POST("api/Curso/notas/comentario")
    Call<ResponseBody> agregarOEditarNotaConComentario(
            @Query("id_usuario_estudiante") int idUsuarioEstudiante,
            @Query("id_usuario_docente") int idUsuarioDocente,
            @Query("id_tema") int idTema,
            @Query("nota") String nota,
            @Query("comentario") String comentario
    );
    @GET("api/Curso/notas/historial")
    Call<List<HistorialNota>> obtenerHistorialNota(
            @Query("idNota") int idNota
    );
    /* ------------------------------- */

    /*CRUD PARA CURSOS*/

    @GET("api/Curso")
    Call<List<CursoMantenimiento>> getCursos();

    @GET("api/Curso/{id}")
    Call<CursoMantenimiento> getCurso(@Path("id") int id);

    @POST("api/Curso")
    Call<CursoMantenimiento> crearCurso(@Body CursoMantenimiento cursoMante);

    @PUT("api/Curso/{id}")
    Call<Void> editarCurso(@Path("id") int id, @Body CursoMantenimiento cursoMante);

    @DELETE("api/Curso/{id}")
    Call<Void> eliminarCurso(@Path("id") int id);

    /* ------------------------------- */

    /*CRUD PARA RAMAS*/
    @GET("api/RamasCurso/porCurso/{idCurso}")
    Call<List<RamaCurso>> getRamasPorCurso(@Path("idCurso") int idCurso);

    @POST("api/RamasCurso")
    Call<RamaCurso> crearRama(@Body RamaCurso rama);

    @PUT("api/RamasCurso/{id}")
    Call<Void> editarRama(@Path("id") int id, @Body RamaCurso rama);

    @DELETE("api/RamasCurso/{id}")
    Call<Void> eliminarRama(@Path("id") int id);
    /* ------------------------------- */

    /*CRUD PARA TEMAS*/
    @GET("api/TemasCurso/porRama/{idRama}")
    Call<List<TemaCurso>> getTemasPorRama(@Path("idRama") int idRama);

    @POST("api/TemasCurso")
    Call<TemaCurso> crearTema(@Body TemaCurso tema);

    @PUT("api/TemasCurso/{id}")
    Call<Void> editarTema(@Path("id") int idTema, @Body TemaEditarDTO tema);
    @DELETE("api/TemasCurso/{id}")
    Call<Void> eliminarTema(@Path("id") int id);
    /* ------------------------------- */
}
