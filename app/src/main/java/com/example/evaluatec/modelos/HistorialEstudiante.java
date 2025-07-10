package com.example.evaluatec.modelos;

public class HistorialEstudiante {
    private int idHistorial;
    private int idUsuarioEstudiante;
    private int idGrado;
    private String gradoNombre;
    private int idSeccion;
    private String seccionNombre;
    private int idAnioEscolar;
    private String estadoAnterior;
    private String estadoNuevo;
    private String accion;
    private String fechaCambio;
    private String nombreResponsable;

    // Getters y Setters

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdUsuarioEstudiante() {
        return idUsuarioEstudiante;
    }

    public void setIdUsuarioEstudiante(int idUsuarioEstudiante) {
        this.idUsuarioEstudiante = idUsuarioEstudiante;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public String getGradoNombre() {
        return gradoNombre;
    }

    public void setGradoNombre(String gradoNombre) {
        this.gradoNombre = gradoNombre;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public String getSeccionNombre() {
        return seccionNombre;
    }

    public void setSeccionNombre(String seccionNombre) {
        this.seccionNombre = seccionNombre;
    }

    public int getIdAnioEscolar() {
        return idAnioEscolar;
    }

    public void setIdAnioEscolar(int idAnioEscolar) {
        this.idAnioEscolar = idAnioEscolar;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(String estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(String fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }
}
