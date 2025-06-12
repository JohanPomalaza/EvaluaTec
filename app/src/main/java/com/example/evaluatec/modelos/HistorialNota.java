package com.example.evaluatec.modelos;

public class HistorialNota {
    private String accion;
    private String notaAnterior;
    private String notaNueva;
    private String comentarioAnterior;
    private String comentarioNuevo;
    private String fechaCambio;
    private String nombreDocente;

    public String getNombreDocente(){
        return nombreDocente;
    }
    public void setNombreDocente(String nombreDocente){
        this.nombreDocente = nombreDocente;
    }

    public String getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(String fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getNotaAnterior() {
        return notaAnterior;
    }

    public void setNotaAnterior(String notaAnterior) {
        this.notaAnterior = notaAnterior;
    }

    public String getNotaNueva() {
        return notaNueva;
    }

    public void setNotaNueva(String notaNueva) {
        this.notaNueva = notaNueva;
    }

    public String getComentarioAnterior() {
        return comentarioAnterior;
    }

    public void setComentarioAnterior(String comentarioAnterior) {
        this.comentarioAnterior = comentarioAnterior;
    }

    public String getComentarioNuevo() {
        return comentarioNuevo;
    }

    public void setComentarioNuevo(String comentarioNuevo) {
        this.comentarioNuevo = comentarioNuevo;
    }
}
