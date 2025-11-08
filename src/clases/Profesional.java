package clases;

import enums.Especialidad;
import interfaces.IConsultarHistoriaClinica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Profesional extends Empleado implements IConsultarHistoriaClinica {
    private String matricula;
    private Especialidad especialidad;
    private List<Disponibilidad> agenda;
    private List<HistoriaClinica<String>> todasLasHistorias;

    public Profesional(String dni, String nombre, String apellido, String nacionalidad, Direccion direccion, String correoElectronico, String contraseña, LocalDate fechaNacimiento, String legajo, String matricula, Especialidad especialidad, List<Disponibilidad> agenda) {
        super(dni, nombre, apellido, nacionalidad, direccion, correoElectronico, contraseña, fechaNacimiento, legajo);
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.agenda = new ArrayList<>();
        this.todasLasHistorias = new ArrayList<>();
    }

    public List<HistoriaClinica<String>> getTodasLasHistorias() {
        return todasLasHistorias;
    }

    public void setTodasLasHistorias(List<HistoriaClinica<String>> todasLasHistorias) {
        this.todasLasHistorias = todasLasHistorias;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
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

    public void verHistoriaClinica(HistoriaClinica<?> historia) {
        System.out.println(historia);
    }

    @Override
    public List<Turno> consultarHistorialTurnos(String dniPaciente) {
        for (HistoriaClinica<String> historia : todasLasHistorias) {
            if (historia.getIdPaciente().equals(dniPaciente)) {
                return historia.getHistorialTurnos();
            }
        }
        return new ArrayList<>(); // si no encuentra la historia
    }

    @Override
    public List<Receta> consultarRecetas(String dniPaciente) {
        for (HistoriaClinica<String> historia : todasLasHistorias) {
            if (historia.getIdPaciente().equals(dniPaciente)) {
                return historia.getRecetasEmitidas();
            }
        }
        return new ArrayList<>();
    }


    @Override
    public String getTipoEmpleado() {
        return "Profesional de la salud";
    }


    public void agregarRecetaAHistoria(HistoriaClinica<?> historia, Receta receta) {
        historia.agregarReceta(receta);
    }

    public void agregarAntecedenteAHistoria(HistoriaClinica<?> historia, Antecedentes antecedente) {
        historia.agregarAntecedente(antecedente);
    }

    public void agregarTurnoAHistoria(HistoriaClinica<?> historia, Turno turno) {
        historia.agregarTurno(turno);
    }

    @Override
    public String toString() {
        return super.toString() +
                "Profesional{" +
                "matricula='" + matricula + '\'' +
                ", especialidad=" + especialidad +
                ", agenda=" + agenda +
                ", todasLasHistorias=" + todasLasHistorias +
                '}';
    }
}







