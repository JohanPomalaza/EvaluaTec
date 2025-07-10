package com.example.evaluatec.modelos;

public class Usuario {
    private String correo;
    private String contrasena;
    private String captchaToken;

    public Usuario(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }
}
