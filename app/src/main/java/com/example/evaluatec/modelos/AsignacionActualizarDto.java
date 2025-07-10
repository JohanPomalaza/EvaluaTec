package com.example.evaluatec.modelos;

public class AsignacionActualizarDto {
    public int idRama;
    public int idGrado;
    public int idSeccion;

    public AsignacionActualizarDto(int idRama, int idGrado,int idSeccion) {
        this.idRama = idRama;
        this.idGrado = idGrado;
        this.idSeccion = idSeccion;
    }
    public AsignacionActualizarDto() {}
}
