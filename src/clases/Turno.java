package clases;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
    private String idTurno;
    private String idPaciente;
    private String idProfesional;
    private LocalDate dia;
    private LocalTime hora;

    //CONSTRUCTOR
    public Turno() {}
    public Turno(String idTurno, String idPaciente, String idProfesional, LocalDate dia, LocalTime hora) {
        this.idTurno = idTurno;
        this.idPaciente = idPaciente;
        this.idProfesional = idProfesional;
        this.dia = dia;
        this.hora = hora;
    }

    //GETTERS Y SETTERS
    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getIdProfesional() {
        return idProfesional;
    }

    public void setIdProfesional(String idProfesional) {
        this.idProfesional = idProfesional;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "Turno{" +
                "idTurno='" + idTurno + '\'' +
                ", idPaciente='" + idPaciente + '\'' +
                ", idProfesional='" + idProfesional + '\'' +
                ", dia=" + dia +
                ", hora=" + hora +
                '}';
    }

    //VER DISPONIBILIDAD DE TURNO
    public boolean estaDisponible() {
        return idPaciente == null;
    }
}
