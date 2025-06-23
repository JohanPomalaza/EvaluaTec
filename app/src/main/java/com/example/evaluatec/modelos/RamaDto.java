package com.example.evaluatec.modelos;

public class RamaDto {
    private int idRama;
    private String nombre;

    public int getIdRama() { return idRama; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre;
    }
}
