package com.example.evaluatec.modelos;

public class AsignacionDto {
    private int idAsignacion;
    private int idRamaCurso;
    private int idGrado;
    private String ramaCursoNombre;
    private String gradoNombre;

    // Getters y setters
    public int getIdAsignacion() { return idAsignacion; }
    public void setIdAsignacion(int idAsignacion) { this.idAsignacion = idAsignacion; }

    public int getIdRamaCurso() { return idRamaCurso; }
    public void setIdRamaCurso(int idRamaCurso) { this.idRamaCurso = idRamaCurso; }

    public int getIdGrado() { return idGrado; }
    public void setIdGrado(int idGrado) { this.idGrado = idGrado; }

    public String getRamaCursoNombre() { return ramaCursoNombre; }
    public void setRamaCursoNombre(String ramaCursoNombre) { this.ramaCursoNombre = ramaCursoNombre; }

    public String getGradoNombre() { return gradoNombre; }
    public void setGradoNombre(String gradoNombre) { this.gradoNombre = gradoNombre; }
}
