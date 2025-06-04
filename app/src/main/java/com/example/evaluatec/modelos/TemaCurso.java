package com.example.evaluatec.modelos;

import com.google.gson.annotations.SerializedName;

public class TemaCurso {
    private int idTema;

    @SerializedName("nombre")
    private String nombre;

    private int idCurso;
    private int idRama;

    // Getters y setters
    public int getIdTema() {
        return idTema;
    }

    public void setIdTema(int idTema) {
        this.idTema = idTema;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getIdRama() {
        return idRama;
    }

    public void setIdRama(int idRama) {
        this.idRama = idRama;
    }

}
