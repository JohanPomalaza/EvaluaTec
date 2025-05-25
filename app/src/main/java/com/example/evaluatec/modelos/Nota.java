package com.example.evaluatec.modelos;

import com.google.gson.annotations.SerializedName;

public class Nota {
    private int idNota;
    private String nota;
    private int idUsuarioEstudiante;
    private int idTema;
    @SerializedName("tema")
    private String tema;

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    @SerializedName("rama")
    private String rama;
    @SerializedName("comentario")
    private String comentario;

    // Getters y Setters
    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
    public String getRama() {
        return rama;
    }

    public void setRama(String rama) {
        this.rama = rama;
    }
    public Nota(int idUsuarioEstudiante, int idTema, String nota) {
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
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
