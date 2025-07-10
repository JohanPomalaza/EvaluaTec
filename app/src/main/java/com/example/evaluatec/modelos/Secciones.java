package com.example.evaluatec.modelos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Secciones {
    @SerializedName("idGrado")
    private int idGrado;

    @SerializedName("grado")
    private String nombreGrado;
    @SerializedName("idSeccion")
    private int idSeccion;
    @SerializedName("seccion")
    private String seccionNombre;

    @SerializedName("idAnioEscolar")
    private int idAnioEscolar;

    // Getters y setters
    public int getIdGrado() { return idGrado; }
    public void setIdGrado(int idGrado) { this.idGrado = idGrado; }

    public String getNombreGrado() { return nombreGrado; }
    public void setNombreGrado(String nombreGrado) { this.nombreGrado = nombreGrado; }

    public int getIdAnioEscolar() { return idAnioEscolar; }
    public void setIdAnioEscolar(int idAnioEscolar) { this.idAnioEscolar = idAnioEscolar; }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public String getSeccionNombre() {
        return seccionNombre;
    }

    public void setSeccionNombre(String seccionNombre) {
        this.seccionNombre = seccionNombre;
    }
}
