package com.example.evaluatec.api;
import com.example.evaluatec.modelos.Alumno;
import com.example.evaluatec.modelos.AnioEscolarDto;
import com.example.evaluatec.modelos.AsignacionActualizarDto;
import com.example.evaluatec.modelos.AsignacionCrearDto;
import com.example.evaluatec.modelos.AsignacionDto;
import com.example.evaluatec.modelos.AsignacionGradoDto;
import com.example.evaluatec.modelos.Curso;
import com.example.evaluatec.modelos.CursoDto;
import com.example.evaluatec.modelos.CursoMantenimiento;
import com.example.evaluatec.modelos.CursoUpdateDTO;
import com.example.evaluatec.modelos.DocenteCrearDto;
import com.example.evaluatec.modelos.DocenteDto;
import com.example.evaluatec.modelos.DocenteEditarDto;
import com.example.evaluatec.modelos.Estudiante;
import com.example.evaluatec.modelos.EstudianteCrearDto;
import com.example.evaluatec.modelos.EstudianteDto;
import com.example.evaluatec.modelos.Grado;
import com.example.evaluatec.modelos.GradoDto;
import com.example.evaluatec.modelos.GradoSeccionDto;
import com.example.evaluatec.modelos.HistorialCurso;
import com.example.evaluatec.modelos.HistorialEstudiante;
import com.example.evaluatec.modelos.HistorialNota;
import com.example.evaluatec.modelos.HistorialRama;
import com.example.evaluatec.modelos.HistorialTema;
import com.example.evaluatec.modelos.NivelDto;
import com.example.evaluatec.modelos.Nota;
import com.example.evaluatec.modelos.NotaPorCurso;
import com.example.evaluatec.modelos.NotificacionDto;
import com.example.evaluatec.modelos.RamaCurso;
import com.example.evaluatec.modelos.RamaCursoCrearDTO;
import com.example.evaluatec.modelos.RamaDto;
import com.example.evaluatec.modelos.RamaEditarDto;
import com.example.evaluatec.modelos.ResumenDocenteDto;
import com.example.evaluatec.modelos.SeccionDto;
import com.example.evaluatec.modelos.Secciones;
import com.example.evaluatec.modelos.TemaCrearDTO;
import com.example.evaluatec.modelos.TemaCurso;
import com.example.evaluatec.modelos.TemaDTO;
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

    @GET("api/Curso/alumno/{idUsuario}/docente/{idDocente}/cursos")
    Call<List<Curso>> getCursosPorAlumnoDocente(@Path("idUsuario") int idUsuario,
                                                @Path("idDocente") int idDocente);
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
    @GET("api/Curso/grado/{id_grado}/seccion/{id_seccion}/anio/{id_anio}/alumnos")
    Call<List<Alumno>> getAlumnosPorGrado(
            @Path("id_grado") int idGrado,
            @Path("id_seccion") int idSeccion,
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
            @Query("id_tema") int idTema,
            @Query("nota") String nota,
            @Query("comentario") String comentario,
            @Query("id_usuario_docente") int idUsuarioDocente,
            @Query("justificacion") String justificacion
    );
    @GET("api/Curso/temas/pendientes")
    Call<List<TemaDTO>> getTemasPendientes(
            @Query("id_usuario_estudiante") int idUsuarioEstudiante,
            @Query("id_rama") int idRama
    );
    @GET("api/Curso/notas/historial")
    Call<List<HistorialNota>> obtenerHistorialNota(
            @Query("idNota") int idNota
    );
    /* ------------------------------- */

    /*CRUD PARA CURSOS*/

    @GET("api/Curso")
    Call<List<CursoMantenimiento>> getCursos();

    @POST("api/Curso")
    Call<CursoMantenimiento> crearCurso(
            @Body CursoMantenimiento curso,
            @Query("id_usuario_admin") int idAdmin
    );

    @PUT("api/Curso/{id}")
    Call<Void> editarCurso(@Path("id") int id, @Body CursoUpdateDTO cursoDto);

    @DELETE("api/Curso/{id}")
    Call<Void> eliminarCurso(
            @Path("id") int id,
            @Query("id_usuario_admin") int idAdmin
    );

    @GET("api/Curso/Historial/{id}")
    Call<List<HistorialCurso>> obtenerHistorialCurso(@Path("id") int idCurso);

    /* ------------------------------- */

    /*CRUD PARA RAMAS*/
    @GET("api/RamasCurso/porCurso/{idCurso}")
    Call<List<RamaCurso>> getRamasPorCurso(@Path("idCurso") int idCurso);

    @POST("api/RamasCurso")
    Call<Void> crearRama(@Body RamaCursoCrearDTO nuevaRama);

    @PUT("api/RamasCurso/{id}/{idUsuario}")
    Call<Void> editarRama(
            @Path("id") int id,
            @Path("idUsuario") int idUsuario,
            @Body RamaEditarDto dto
    );

    @DELETE("api/RamasCurso/{id}/{idUsuario}")
    Call<Void> eliminarRama(@Path("id") int id,
                            @Path("idUsuario") int idUsuario );

    @GET("api/RamasCurso/historial/{idRama}")
    Call<List<HistorialRama>> getHistorialRama(@Path("idRama") int idRama);
    /* ------------------------------- */

    /*CRUD PARA TEMAS*/
    @GET("api/TemasCurso/porRama/{idRama}")
    Call<List<TemaCurso>> getTemasPorRama(@Path("idRama") int idRama);

    @POST("api/TemasCurso/{idUsuario}")
    Call<TemaCurso> crearTema(@Path("idUsuario") int idUsuario, @Body TemaCrearDTO temaDTO);

    @PUT("api/TemasCurso/{id}/{idUsuario}")
    Call<Void> editarTema(@Path("id") int idTema,
                          @Path("idUsuario") int idUsuario,
                          @Body TemaEditarDTO tema);
    @DELETE("api/TemasCurso/{id}/{idUsuario}")
    Call<Void> eliminarTema(@Path("id") int id,
                            @Path("idUsuario") int idUsuario);

    @GET("api/TemasCurso/historial/{idTema}")
    Call<List<HistorialTema>> getHistorialTema(@Path("idTema") int idTema);

    @GET("api/TemasCurso/grados/porNivel/{idNivel}")
    Call<List<Grado>> getGradosPorNivel(@Path("idNivel") int idNivel);
    /* ------------------------------- */

    /*CRUD PARA DOCENTES*/
    @GET("api/docentes/lista")
    Call<List<DocenteDto>> getDocentes();

    @POST("api/docentes")
    Call<Void> crearDocente(@Body DocenteCrearDto docente);

    @PUT("api/docentes/{id}")
    Call<Void> editarDocente(@Path("id") int id, @Body DocenteEditarDto docente);

    @PUT("api/docentes/{id}/inactivar")
    Call<Void> inactivarDocente(@Path("id") int idDocente);

    @POST("api/docentes/{idDocente}/asignar")
    Call<Void> asignarCurso(@Path("idDocente") int idDocente, @Body AsignacionCrearDto asignacion);
    @GET("api/Docentes/{idDocente}/asignaciones")
    Call<List<AsignacionDto>> getAsignacionesPorDocente(@Path("idDocente") int idDocente);

    @PUT("api/docentes/asignacion/{idAsignacion}")
    Call<Void> actualizarAsignacion(@Path("idAsignacion") int idAsignacion, @Body AsignacionActualizarDto dto);
    @GET("api/docentes/cursos/{idCurso}/ramas")
    Call<List<RamaDto>> getRamasPorCursoDoc(@Path("idCurso") int idCurso);

    @GET("api/Docentes/grados")
    Call<List<GradoDto>> getGrados();

    @GET("api/Docentes/grados/{idGrado}/secciones")
    Call<List<SeccionDto>> getSeccionesPorGrado(@Path("idGrado") int idGrado);

    @GET("api/Docentes/porNivel/{idNivel}")
    Call<List<GradoSeccionDto>> getGradosYSeccionesPorNivel(@Path("idNivel") int idNivel);

    @GET("api/Docentes/niveles")
    Call<List<NivelDto>> getNiveles();

    @GET("api/Docentes/anios")
    Call<List<AnioEscolarDto>> getAniosEscolares();

    @GET("api/docentes/cursos/porNivel/{idNivel}")
    Call<List<CursoDto>> getCursosPorNivel(@Path("idNivel") int idNivel);

    @PUT("api/docentes/asignacion/{idAsignacion}/inactivar")
    Call<Void> eliminarAsignacion(@Path("idAsignacion") int idAsignacion);

    @GET("api/docentes/{idDocente}/resumen")
    Call<ResumenDocenteDto> obtenerResumenDocente(@Path("idDocente") int idDocente);
    /*-----------------------------------------------------------------------------------------------------------------------------*/

    /*-----CRUD ESTUDIANTE-----*/

    @GET("api/Estudiantes")
    Call<List<EstudianteDto>> obtenerEstudiantes();

    @POST("api/Estudiantes")
    Call<Void> crearEstudiante(@Body EstudianteCrearDto estudiante);

    @PUT("api/Estudiantes/{id}")
    Call<Void> editarEstudiante(@Path("id") int id, @Body EstudianteCrearDto estudiante);
    @GET("api/Estudiantes/Grados")  // o la ruta que elijas
    Call<List<GradoDto>> obtenerGrados();
    @POST("api/Estudiantes/{idEstudiante}/asignar")
    Call<Void> asignarGrado(
            @Path("idEstudiante") int idEstudiante,
            @Body AsignacionGradoDto dto
    );
    @PUT("api/Estudiantes/{idEstudiante}/inactivar")
    Call<Void> inactivarEstudiante(@Path("idEstudiante") int idEstudiante);
    @GET("api/Estudiantes/{idEstudiante}/historial")
    Call<List<HistorialEstudiante>> getHistorialEstudiante(@Path("idEstudiante") int idEstudiante);
    /*-----NOTIFICACIONES-----*/

    @PUT("api/curso/notificaciones/{id}/leer")
    Call<Void> marcarComoLeida(@Path("id") int idNotificacion);
    @GET("api/curso/notificaciones/estudiante/{id}/todas")
    Call<List<NotificacionDto>> getHistorialNotificaciones(@Path("id") int idUsuario);
    @GET("api/curso/notificaciones/estudiante/{id}")
    Call<List<NotificacionDto>> getNotificacionesNoLeidas(@Path("id") int idUsuario);
}
