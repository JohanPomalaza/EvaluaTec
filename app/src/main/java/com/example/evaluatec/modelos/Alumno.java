package com.example.evaluatec.modelos;

import com.google.gson.annotations.SerializedName;

public class Alumno {
    @SerializedName("idUsuarioEstudiante")
    private int idUsuarioEstudiante;

    @SerializedName("nombreCompleto")
    private String nombreCompleto;

    public int getIdUsuarioEstudiante() {
        return idUsuarioEstudiante;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }
}
