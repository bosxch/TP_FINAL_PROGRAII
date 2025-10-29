package Interfaz;

import Enums.Especialidad;
import clases.Profesional;
import clases.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ISacarTurnos {

    List<Profesional> consultarEspecialidad(Especialidad especialidad);

    Turno sacarTurno(String idProfesional, LocalDate dia, LocalTime hora);

    boolean editarTurno(String idTurno, Turno nuevoTurno);
}
