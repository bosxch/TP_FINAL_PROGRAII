package clases;

import excepciones.CredencialesIncorrectasException;

import java.time.LocalDate;
import java.time.Period;

public abstract class Persona {
    protected String dni;
    protected String nombre;
    protected String apellido;
    protected String nacionalidad;
    protected Direccion direccion;
    protected String correoElectronico;
    protected String contraseniaEncriptada;
    protected LocalDate fechaNacimiento;

    // Constructor
    public Persona(String dni, String nombre, String apellido, String nacionalidad,
                   Direccion direccion, String correoElectronico, String contrasenia,
                   LocalDate fechaNacimiento) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.direccion = direccion;
        this.correoElectronico = correoElectronico;
        this.contraseniaEncriptada = Seguridad.encriptar(contrasenia);
        this.fechaNacimiento = fechaNacimiento;
    }

    public Persona() {

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

    public abstract String getTipo();

    // Calcula la edad dinamicamente
    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    //  Verifica la contraseña encriptado contra la ingresada por el usurio
    public boolean verificarContrasenia(String contraseñaIngresada) {
        return Seguridad.encriptar(contraseñaIngresada).equals(this.contraseniaEncriptada);
    }

    // Para actualiar la contraseña primero, se verfica la actual, luego se modifica y se encripta
    public boolean cambiarContrasenia(String actual, String nueva) {
        if (verificarContrasenia(actual)) {
            this.contraseniaEncriptada = Seguridad.encriptar(nueva);
            return true;
        }
        return false;
    }
    public boolean login(String email, String contrasenia) throws CredencialesIncorrectasException {
        // Se valida primero el correo y luego la contrasenia
        // Se usa equalsIgnoreCase para ser flexible con el correo, ignorando mayusculas y minisculas
        if (this.correoElectronico.equalsIgnoreCase(email) && verificarContrasenia(contrasenia)) {
            // login correcto
            return true;
        } else {
            // Si falla la validación del correo o la contraseña
            throw new CredencialesIncorrectasException("El email o la contrasenia ingresados son incorrectos.");
        }
    }

    public String getContraseniaEncriptada() {
        return contraseniaEncriptada;
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

