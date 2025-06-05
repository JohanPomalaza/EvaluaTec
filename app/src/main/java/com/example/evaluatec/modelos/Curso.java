package com.example.evaluatec.modelos;

import com.google.gson.annotations.SerializedName;

public class Curso {
    private int idCurso;


    @SerializedName("nombreCurso")
    private String curso;
    @SerializedName("curso")
    private String nombreCurso;
    private int idUsuario;

    @SerializedName("idRama")
    private int idRama;

    @SerializedName("rama")
    private String rama;

    // Getters y Setters
    public int getIdCurso() {
        return idCurso;
    }
    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }
    public String getNombreCurso() {
        return nombreCurso;
    }
    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public int getIdUsuario() {return idUsuario;}
    public void setIdUsuario(int idUsuario) {this.idUsuario = idUsuario;}

    public int getIdRama() {
        return idRama;
    }

    public String getRama() {
        return rama;
    }
}
