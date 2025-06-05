package com.example.evaluatec.modelos;

import com.google.gson.annotations.SerializedName;

public class TemaEditarDTO {
    @SerializedName("idTema")
    private int idTema;

    @SerializedName("nombre")
    private String nombre;

    private int idRama;
    public TemaEditarDTO() {
    }

    public TemaEditarDTO(int idTema, String nombre) {
        this.idTema = idTema;
        this.nombre = nombre;
    }

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
    public int getIdRama() {
        return idRama;
    }

    public void setIdRama(int idRama) {
        this.idRama = idRama;
    }
}
