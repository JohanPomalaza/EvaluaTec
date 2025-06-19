package com.example.evaluatec.modelos;

import com.google.gson.annotations.SerializedName;

public class RamaCurso {
    @SerializedName("idRama")
    private int idRama;

    @SerializedName("idCurso")
    private int idCurso;

    @SerializedName("nombre")
    private String nombreRama;

    @SerializedName("curso")
    private RamaMantenimiento ramaMantenimiento;

    private int idGrado;

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    private int usuarioId;

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    // Getters y Setters
    public int getIdRama() { return idRama; }
    public void setIdRama(int idRama) { this.idRama = idRama; }

    public String getNombreRama() { return nombreRama; }
    public void setNombreRama(String nombreRama) { this.nombreRama = nombreRama; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public RamaMantenimiento getCurso() {
        return ramaMantenimiento;
    }

    public void setCurso(RamaMantenimiento curso) {
        this.ramaMantenimiento = curso;
    }
}
