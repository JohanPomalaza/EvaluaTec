package com.example.evaluatec.modelos;

public class NivelEducativo {
    private int id;
    private String nombre;

    public NivelEducativo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
}
