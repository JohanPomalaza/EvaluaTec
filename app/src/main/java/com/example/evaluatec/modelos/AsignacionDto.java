package com.example.evaluatec.modelos;

public class AsignacionDto {
    private int idAsignacion;
    private int idRamaCurso;
    private int idGrado;
    private String ramaCursoNombre;
    private String gradoNombre;
    private int idCurso;
    private String cursoNombre;
    private int idNivel;
    private String nivelNombre;
    private int idAnioEscolar;
    private String anioEscolarNombre;

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public String getNivelNombre() {
        return nivelNombre;
    }

    public void setNivelNombre(String nivelNombre) {
        this.nivelNombre = nivelNombre;
    }

    public int getIdAnioEscolar() {
        return idAnioEscolar;
    }

    public void setIdAnioEscolar(int idAnioEscolar) {
        this.idAnioEscolar = idAnioEscolar;
    }

    public String getAnioEscolarNombre() {
        return anioEscolarNombre;
    }

    public void setAnioEscolarNombre(String anioEscolarNombre) {
        this.anioEscolarNombre = anioEscolarNombre;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreCurso() {
        return cursoNombre;
    }

    public void setNombreCurso(String nombreCurso) {
        cursoNombre = nombreCurso;
    }

    private int idSeccion;
    private String seccionNombre;

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public String getSeccionNombre() {
        return seccionNombre;
    }

    public void setSeccionNombre(String seccionNombre) {
        this.seccionNombre = seccionNombre;
    }

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
    public interface HasId {
        int getId();
    }
}
