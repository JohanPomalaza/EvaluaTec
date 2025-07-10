package com.example.evaluatec.modelos;

public class AsignacionGradoDto {
    private int idGrado;
    private int idAnioEscolar;

    private int idSeccion;
    private int usuarioResponsable;

    public int getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(int usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public AsignacionGradoDto(int idGrado, int idAnioEscolar, int idSeccion, int usuarioResponsable) {
        this.idGrado = idGrado;
        this.idAnioEscolar = idAnioEscolar;
        this.idSeccion = idSeccion;
        this.usuarioResponsable = usuarioResponsable;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public int getIdAnioEscolar() {
        return idAnioEscolar;
    }

    public void setIdAnioEscolar(int idAnioEscolar) {
        this.idAnioEscolar = idAnioEscolar;
    }
}
