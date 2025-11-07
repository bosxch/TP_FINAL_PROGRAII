package clases;

import enums.Especialidad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Profesional extends Empleado{
    private String matricula;
    private Especialidad especialidad;
    private List<Disponibilidad> agenda;

    public Profesional(String dni, String nombre, String apellido, String nacionalidad, Direccion direccion, String correoElectronico, String contraseña, LocalDate fechaNacimiento, String legajo, String matricula, Especialidad especialidad, List<Disponibilidad> agenda) {
        super(dni, nombre, apellido, nacionalidad, direccion, correoElectronico, contraseña, fechaNacimiento, legajo);
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.agenda = new ArrayList<>();
    }
    public Profesional() {
        this.agenda = new ArrayList<>();
    }
    public void mostrarDisponibilidad() {
        System.out.println("---Agenda del Dr. " + this.getApellido() + " (" + this.especialidad + ") ---");
        if (agenda.isEmpty()) {
            System.out.println("Agenda vacia.");
            return;
        }
        for (Disponibilidad d : agenda) {
            System.out.println(d);
        }
    }
    @Override
    public String getTipoEmpleado() {
        return "Profesional de la salud";
    }
}
