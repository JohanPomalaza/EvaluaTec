package com.example.evaluatec.modelos;

import com.google.gson.annotations.SerializedName;

public class Nota {
    private int idNota;
    private double nota;
    private int idUsuarioEstudiante;
    private int idTema;

    @SerializedName("nombre")
    private String tema;

    @SerializedName("rama")
    private String rama;

    // Getters y Setters
    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getRama() {
        return rama;
    }

    public void setRama(String rama) {
        this.rama = rama;
    }
    public Nota(int idUsuarioEstudiante, int idTema, double nota) {
        this.idUsuarioEstudiante = idUsuarioEstudiante;
        this.idTema = idTema;
        this.nota = nota;
    }

    public int getIdUsuarioEstudiante() {
        return idUsuarioEstudiante;
    }

    public void setIdUsuarioEstudiante(int idUsuarioEstudiante) {
        this.idUsuarioEstudiante = idUsuarioEstudiante;
    }

    public int getIdTema() {
        return idTema;
    }

    public void setIdTema(int idTema) {
        this.idTema = idTema;
    }
}
