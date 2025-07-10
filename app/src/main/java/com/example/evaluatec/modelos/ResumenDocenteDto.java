package com.example.evaluatec.modelos;

public class ResumenDocenteDto {
    private int cursosAsignados;
    private int alumnosAsignados;

    public int getCursosAsignados() {
        return cursosAsignados;
    }

    public void setCursosAsignados(int cursosAsignados) {
        this.cursosAsignados = cursosAsignados;
    }

    public int getAlumnosAsignados() {
        return alumnosAsignados;
    }

    public void setAlumnosAsignados(int alumnosAsignados) {
        this.alumnosAsignados = alumnosAsignados;
    }
}
