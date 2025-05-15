package com.example.evaluatec.modelos;

import java.util.List;

public class NotaEstudiante {
    private String nombre;
    private String apellido;
    private List<Nota> notas;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public List<Nota> getNotas() { return notas; }
    public void setNotas(List<Nota> notas) { this.notas = notas; }
}
