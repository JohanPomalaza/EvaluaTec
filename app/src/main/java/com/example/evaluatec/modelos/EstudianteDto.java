package com.example.evaluatec.modelos;

public class EstudianteDto {
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private AsignacionDto asignacion;
    private int idUsuarioEstudiante;

    public int getIdUsuarioEstudiante() {
        return idUsuarioEstudiante;
    }

    public void setIdUsuarioEstudiante(int idUsuarioEstudiante) {
        this.idUsuarioEstudiante = idUsuarioEstudiante;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public AsignacionDto getAsignacion() { return asignacion; }
    public void setAsignacion(AsignacionDto asignacion) { this.asignacion = asignacion; }
}
