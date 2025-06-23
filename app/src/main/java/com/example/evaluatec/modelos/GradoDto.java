package com.example.evaluatec.modelos;

public class GradoDto {
    private int idGrado;
    private String nombreGrado;

    public int getIdGrado() { return idGrado; }
    public String getNombreGrado() { return nombreGrado; }

    @Override
    public String toString() {
        return nombreGrado;
    }
}
