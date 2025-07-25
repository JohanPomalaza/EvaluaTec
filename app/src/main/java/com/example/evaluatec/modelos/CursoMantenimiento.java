package com.example.evaluatec.modelos;

import com.google.gson.annotations.SerializedName;

public class CursoMantenimiento {
    @SerializedName("idCurso")
    private int idCurso;

    @SerializedName("nombreCurso")
    private String nombreCurso;
    private int idUsuario;

    @SerializedName("idRama")
    private int idRama;
    @SerializedName("idNivel")
    private int idNivel;

    public int getIdNivel() {
        return idNivel;
    }


    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }



    @SerializedName("rama")
    private String rama;

    // Getters y Setters
    public int getIdCurso() {
        return idCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }
    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }
    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
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
