package com.example.evaluatec.modelos;

public class HistorialTema {
    private int idHistorial;
    private int idTema;
    private String accion;
    private String nombreAnterior;
    private String nombreNuevo;
    private boolean estadoAnterior;
    private boolean estadoNuevo;
    private String fechaCambio;
    private int idUsuario;
    private String nombreUsuario;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdTema() {
        return idTema;
    }

    public void setIdTema(int idTema) {
        this.idTema = idTema;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getNombreAnterior() {
        return nombreAnterior;
    }

    public void setNombreAnterior(String nombreAnterior) {
        this.nombreAnterior = nombreAnterior;
    }

    public String getNombreNuevo() {
        return nombreNuevo;
    }

    public void setNombreNuevo(String nombreNuevo) {
        this.nombreNuevo = nombreNuevo;
    }

    public boolean isEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(boolean estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(String fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public boolean isEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(boolean estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
