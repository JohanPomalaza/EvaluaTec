package com.example.evaluatec.api;

public class LoginResponse {
    private String mensaje;
    private int id;
    private String rol;

    private String nombre;
    private String apellido;

    public String getMensaje() {
        return mensaje;
    }

    public int getId() {
        return id;
    }

    public String getRol() {
        return rol;
    }
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
}
