package clases;

import java.time.LocalDate;

public abstract class Empleado extends Persona {
    protected String legajo;



    public Empleado() {
        super();
    }

    public Empleado(String dni, String nombre, String apellido, String nacionalidad, Direccion direccion, String correoElectronico, String contrasenia, LocalDate fechaNacimiento, String legajo) {
        super(dni, nombre, apellido, nacionalidad, direccion, correoElectronico, contrasenia, fechaNacimiento);
        this.legajo = legajo;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }
    public abstract String getTipoEmpleado();

    @Override
    public String toString() {
        return "Empleado{" +
                "legajo='" + legajo + '\'' +
                '}';
    }
}
