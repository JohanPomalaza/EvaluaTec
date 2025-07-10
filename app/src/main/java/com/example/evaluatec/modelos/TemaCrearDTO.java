package com.example.evaluatec.modelos;

public class TemaCrearDTO {
    private String nombre;
    private int idRama;
    private int idGrado;

    public int getOrden() {
        return Orden;
    }

    public void setOrden(int orden) {
        Orden = orden;
    }

    private int Orden;

    public TemaCrearDTO(String nombre, int idRama, int idGrado, int orden) {
        this.nombre = nombre;
        this.idRama = idRama;
        this.idGrado = idGrado;
        this.Orden = orden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdRama() {
        return idRama;
    }

    public void setIdRama(int idRama) {
        this.idRama = idRama;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }
}
