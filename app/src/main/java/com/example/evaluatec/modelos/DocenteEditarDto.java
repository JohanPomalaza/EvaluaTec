package com.example.evaluatec.modelos;

public class DocenteEditarDto {
    public String nombre;
    public String apellido;
    public String correo;
    public String contrasena;

    public DocenteEditarDto(String nombre, String apellido, String correo, String contrasena) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
    }
}
