package com.example.evaluatec.modelos;

public class GradoSeccionDto {
    private int idGrado;
    private int idSeccion;
    private String nombreGrado;
    private String nombreSeccion;

    @Override
    public String toString() {
        return nombreGrado + " - Sección " + nombreSeccion;
    }

    public int getIdGrado() { return idGrado; }
    public void setIdGrado(int idGrado) { this.idGrado = idGrado; }
    public int getIdSeccion() { return idSeccion; }
    public void setIdSeccion(int idSeccion) { this.idSeccion = idSeccion; }
    public String getNombreGrado() { return nombreGrado; }
    public void setNombreGrado(String nombreGrado) { this.nombreGrado = nombreGrado; }
    public String getNombreSeccion() { return nombreSeccion; }
    public void setNombreSeccion(String nombreSeccion) { this.nombreSeccion = nombreSeccion; }
}
