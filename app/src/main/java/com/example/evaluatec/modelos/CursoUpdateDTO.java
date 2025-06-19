package com.example.evaluatec.modelos;

public class CursoUpdateDTO {
    private String nombreCurso;
    private int usuarioResponsable;

    public CursoUpdateDTO(String nombreCurso, int usuarioResponsable) {
        this.nombreCurso = nombreCurso;
        this.usuarioResponsable = usuarioResponsable;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public int getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(int usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }
}
