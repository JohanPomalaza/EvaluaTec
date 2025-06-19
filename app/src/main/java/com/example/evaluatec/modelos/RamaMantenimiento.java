package com.example.evaluatec.modelos;

import com.google.gson.annotations.SerializedName;

public class RamaMantenimiento {
    @SerializedName("idCurso")
    private int idCurso;

    @SerializedName("nombreCurso")
    private String nombreCurso;

    private int usuarioId;

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }


}
