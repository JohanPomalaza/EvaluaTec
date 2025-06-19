package com.example.evaluatec.modelos;

public class RamaCursoCrearDTO {
    private int idCurso;
    private int idUsuario;
    private String nombre;

    // Constructor (opcional, para mayor comodidad)
    public RamaCursoCrearDTO(int idCurso, int idUsuario, String nombre) {
        this.idCurso = idCurso;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
    }

    // Getters y setters
    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    @Override
    public String toString() {
        return "RamaCursoCrearDto{" +
                "idCurso=" + idCurso +
                ", idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
