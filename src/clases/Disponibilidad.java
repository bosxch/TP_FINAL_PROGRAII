package clases;

import excepciones.TurnoNoDisponibleException;

import java.time.LocalDate;
import java.time.LocalTime;

// Modela un SLOTS de tiempo que el profesional tiene libre u ocupado
public class Disponibilidad {
    private String idDisponibilidad;
    private LocalDate dia;
    private LocalTime horario;
    private boolean isDisponible; // true si está libre, false si está ocupado/reservado
    private Turno turnoAsignado; // Referencia al turno si isDisponible es false

    // Constructor vacio
    public Disponibilidad() {
        this.isDisponible = true; // Por defecto está libre
    }

    // Constructor para definir un slot
    public Disponibilidad(String id, LocalDate dia, LocalTime horario) {
        this.idDisponibilidad = id;
        this.dia = dia;
        this.horario = horario;
        this.isDisponible = true;
        this.turnoAsignado = null;
    }

    public void asignarTurno(Turno turno) throws TurnoNoDisponibleException {
        if (!this.isDisponible) {
            // El requisito pide usar una excepción personalizada
            throw new TurnoNoDisponibleException("El horario (" + this.horario + ") ya se encuentra reservado.");
        }
        // Si está libre, procede con la asignacion
        this.turnoAsignado = turno;
        this.isDisponible = false;
        System.out.println("Slot reservado con exito para el turno: " + turno.getIdTurno());
    }

    // Metodo para liberar el slot
    public void liberarTurno() {
        this.turnoAsignado = null;
        this.isDisponible = true;
    }

    @Override
    public String toString() {
        return "Disponibilidad{" +
                "id='" + idDisponibilidad + '\'' +
                ", Dia=" + dia +
                ", Horario=" + horario +
                ", Disponible=" + isDisponible +
                '}';
    }
}