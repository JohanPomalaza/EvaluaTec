package com.example.evaluatec.modelos;

public class AnioEscolarDto {
    private int idAnioEscolar;
    private int anio;

    @Override
    public String toString() {
        return String.valueOf(anio);
    }

    public int getIdAnioEscolar() { return idAnioEscolar; }
    public void setIdAnioEscolar(int idAnioEscolar) { this.idAnioEscolar = idAnioEscolar; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
}
