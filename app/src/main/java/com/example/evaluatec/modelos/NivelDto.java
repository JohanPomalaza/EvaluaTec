package com.example.evaluatec.modelos;

public class NivelDto {
    private int idNivel;
    private String nombreNivel;

    @Override
    public String toString() {
        return nombreNivel;
    }

    public int getIdNivel() { return idNivel; }
    public void setIdNivel(int idNivel) { this.idNivel = idNivel; }
    public String getNombreNivel() { return nombreNivel; }
    public void setNombreNivel(String nombreNivel) { this.nombreNivel = nombreNivel; }
}
