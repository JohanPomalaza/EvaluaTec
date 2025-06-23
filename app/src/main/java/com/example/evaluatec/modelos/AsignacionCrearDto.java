package com.example.evaluatec.modelos;

public class AsignacionCrearDto {
    public int idUsuarioDocente;
    public int idRama;
    public int idGrado;
    public int idAsignador;
    public int idAnioEscolar;

    public AsignacionCrearDto(int idUsuarioDocente,int idAsignador, int idRama, int idGrado, int idAnioEscolar) {
        this.idUsuarioDocente = idUsuarioDocente;
        this.idAsignador = idAsignador;
        this.idRama = idRama;
        this.idGrado = idGrado;
        this.idAnioEscolar = idAnioEscolar;
    }
}
