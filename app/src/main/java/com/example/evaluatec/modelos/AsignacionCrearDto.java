package com.example.evaluatec.modelos;

import java.util.List;

public class AsignacionCrearDto {
    private int idUsuarioDocente;
    private int idGrado;
    private int idSeccion;
    private int idAsignador;
    private int idAnioEscolar;
    private List<Integer> idRamas;

    public AsignacionCrearDto() {
    }

    public AsignacionCrearDto(int idUsuarioDocente, int idGrado, int idSeccion, int idAsignador, int idAnioEscolar, List<Integer> idRamas) {
        this.idUsuarioDocente = idUsuarioDocente;
        this.idGrado = idGrado;
        this.idSeccion = idSeccion;
        this.idAsignador = idAsignador;
        this.idAnioEscolar = idAnioEscolar;
        this.idRamas = idRamas;
    }

    public int getIdUsuarioDocente() {
        return idUsuarioDocente;
    }

    public void setIdUsuarioDocente(int idUsuarioDocente) {
        this.idUsuarioDocente = idUsuarioDocente;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public int getIdAsignador() {
        return idAsignador;
    }

    public void setIdAsignador(int idAsignador) {
        this.idAsignador = idAsignador;
    }

    public int getIdAnioEscolar() {
        return idAnioEscolar;
    }

    public void setIdAnioEscolar(int idAnioEscolar) {
        this.idAnioEscolar = idAnioEscolar;
    }

    public List<Integer> getIdRamas() {
        return idRamas;
    }

    public void setIdRamas(List<Integer> idRamas) {
        this.idRamas = idRamas;
    }
}
