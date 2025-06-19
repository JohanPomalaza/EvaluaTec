package com.example.evaluatec.modelos;

public class RamaEditarDto {
    private int idCurso;
    private String nombre;

    public RamaEditarDto(int idCurso, String nombre) {
        this.idCurso = idCurso;
        this.nombre = nombre;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}
