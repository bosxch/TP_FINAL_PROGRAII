package clases;

import java.time.LocalDate;

public class Administrativo extends Empleado {

    private String sector;

    public Administrativo(String dni, String nombre, String apellido, String nacionalidad,
                          Direccion direccion, String correoElectronico, String contrasenia,
                          LocalDate fechaNacimiento, String legajo, String sector) {

        super(dni, nombre, apellido, nacionalidad, direccion, correoElectronico, contrasenia, fechaNacimiento, legajo);
        this.sector = sector;
    }

    public Administrativo() {
        super();
    }

    @Override
    public String getTipoEmpleado() {
        return "Personal Administrativo";
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    @Override
    public String toString() {
        return super.toString() + " | Sector: " + sector;
    }
}