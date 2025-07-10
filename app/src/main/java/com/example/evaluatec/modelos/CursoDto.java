package com.example.evaluatec.modelos;

public class CursoDto {
    private int idCurso;
    private String nombreCurso;
    private int idNivel;

    @Override
    public String toString() {
        return nombreCurso;
    }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }
    public String getNombreCurso() { return nombreCurso; }
    public int getIdNivel() { return idNivel; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }
}
