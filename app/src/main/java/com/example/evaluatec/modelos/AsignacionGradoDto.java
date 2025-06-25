package com.example.evaluatec.modelos;

public class AsignacionGradoDto {
    private int idGrado;
    private int idAnioEscolar;

    public AsignacionGradoDto(int idGrado, int idAnioEscolar) {
        this.idGrado = idGrado;
        this.idAnioEscolar = idAnioEscolar;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public int getIdAnioEscolar() {
        return idAnioEscolar;
    }

    public void setIdAnioEscolar(int idAnioEscolar) {
        this.idAnioEscolar = idAnioEscolar;
    }
}
