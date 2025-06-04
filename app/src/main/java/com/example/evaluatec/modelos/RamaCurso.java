package com.example.evaluatec.modelos;

import com.google.gson.annotations.SerializedName;

public class RamaCurso {
    @SerializedName("idRama")
    private int idRama;
    @SerializedName("idCurso")
    private int idCurso;
    @SerializedName("nombre")
    private String nombreRama;

    // Getters y Setters
    public int getIdRama() { return idRama; }
    public void setIdRama(int idRama) { this.idRama = idRama; }

    public String getNombreRama() { return nombreRama; }
    public void setNombreRama(String nombreRama) { this.nombreRama = nombreRama; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }
}
