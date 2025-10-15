package clases;

import java.time.LocalDate;
import java.time.Period;

public abstract class Persona {
    protected String dni;
    protected String nombre;
    protected String apellido;
    protected String nacionalidad;
    protected Direccion direccion;
    protected String correoElectronico;
    protected String contraseñaEncriptada;
    protected LocalDate fechaNacimiento;

    // Constructor
    public Persona(String dni, String nombre, String apellido, String nacionalidad,
                   Direccion direccion, String correoElectronico, String contraseña,
                   LocalDate fechaNacimiento) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.direccion = direccion;
        this.correoElectronico = correoElectronico;
        this.contraseñaEncriptada = Seguridad.encriptar(contraseña);
        this.fechaNacimiento = fechaNacimiento;
    }

    // *** Getters ***
    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    // Calcula la edad dinámicamente
    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    // *** Setters ***
    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    //  Verifica la contraseña encriptado contra la ingresada por el usurio
    public boolean verificarContraseña(String contraseñaIngresada) {
        return Seguridad.encriptar(contraseñaIngresada).equals(this.contraseñaEncriptada);
    }

    // Para actualiar la contraseña primero, se verfica la actual, luego se modifica y se encripta
    public boolean cambiarContraseña(String actual, String nueva) {
        if (verificarContraseña(actual)) {
            this.contraseñaEncriptada = Seguridad.encriptar(nueva);
            return true;
        }
        return false;
    }

    public String getContraseñaEncriptada() {
        return contraseñaEncriptada;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " - DNI: " + dni +
                " - Edad: " + getEdad() +
                " - Nacionalidad: " + nacionalidad +
                " - Correo: " + correoElectronico +
                " - Dirección: " + direccion;
    }
}

