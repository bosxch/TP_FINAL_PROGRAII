package clases;

import enums.Especialidad;
import interfaces.IConsultarHistoriaClinica;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profesional extends Empleado implements IConsultarHistoriaClinica {
    private String matricula;
    private Especialidad especialidad;
    private List<Turno> agenda;

    private List<HistoriaClinica<String>> todasLasHistorias;

    public Profesional(String dni, String nombre, String apellido, String nacionalidad, Direccion direccion, String correoElectronico, String contraseña, LocalDate fechaNacimiento, String legajo, String matricula, Especialidad especialidad, List<Turno> agenda) {
        super(dni, nombre, apellido, nacionalidad, direccion, correoElectronico, contraseña, fechaNacimiento, legajo);
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.agenda = generarAgenda();
        this.todasLasHistorias = new ArrayList<>();
    }

    public List<HistoriaClinica<String>> getTodasLasHistorias() {
        return todasLasHistorias;
    }

    public void setTodasLasHistorias(List<HistoriaClinica<String>> todasLasHistorias) {
        this.todasLasHistorias = todasLasHistorias;
    }

    public Profesional() {
        this.agenda = new ArrayList<>();
    }

    // GENERA AGENDA PARA LOS PROXIMOS 5 DÌAS HÀBILES
    private List<Turno> generarAgenda()
    {
        List<Turno> agenda = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusMonths(1);
        String idProfesional = this.matricula;

        while (!hoy.isAfter(limite))
        {
            if (hoy.getDayOfWeek() != DayOfWeek.SATURDAY && hoy.getDayOfWeek() != DayOfWeek.SUNDAY) continue;

            for (int i = 8; i < 13; i++)
                {
                    Turno turno = new Turno(UUID.randomUUID().toString(), null, idProfesional, hoy, LocalTime.of(i, 0));
                    agenda.add(turno);
                }
            hoy = hoy.plusDays(1);
        }
        return agenda;
    }

    //METODO PARA ACTUALIZAR AGENDA UN MES MAS
    public void actualizarAgendaMensual()
    {
        LocalDate hoy = LocalDate.now();
        if(hoy.getDayOfMonth() == 15)
        {
            LocalDate ultimoDia = agenda.stream()
                    .map(Turno::getDia)
                    .max(LocalDate::compareTo)
                    .orElse(hoy);
            LocalDate limite = hoy.plusMonths(1);

            while (!ultimoDia.isAfter(limite)) {
                if (ultimoDia.getDayOfWeek() != DayOfWeek.SATURDAY && ultimoDia.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    final LocalDate diaActual = ultimoDia;

                    for (int j = 8; j < 13; j++) {
                        LocalTime horaTurno = LocalTime.of(j, 0);

                        boolean existe = agenda.stream().anyMatch(t ->
                                t.getDia().equals(diaActual) && t.getHora().equals(horaTurno)
                        );

                        if (!existe) {
                            agenda.add(new Turno(UUID.randomUUID().toString(), null, matricula, ultimoDia, horaTurno));
                        }
                    }
                }
                ultimoDia = ultimoDia.plusDays(1);
            }

        }
    }

    // METODO PARA ELIMINAR TURNOS PREVIOS
    public void eliminarTurnosPasados() {
        LocalDate hoy = LocalDate.now();
        agenda.removeIf(t -> t.getDia().isBefore(hoy));
    }

    // METODO PARA MOSTRAR TURNOS
    public int mostrarDisponibilidad() {
        eliminarTurnosPasados();
        actualizarAgendaMensual();
        int i = 1;
        int cantidadDisponibles = 0;

        boolean hayDisponibles = false;
        for (Turno t : agenda) {

            if (t.getIdPaciente() == null && !t.getDia().isBefore(LocalDate.now())) {
                System.out.println(i + " - " + t.getDia() + " " + t.getHora());
                hayDisponibles = true;
                cantidadDisponibles++;
            }
        }
        if (!hayDisponibles) {
            System.out.println("No hay turnos disponibles actualmente.");
        }
        return cantidadDisponibles;
    }

    //METODO PARA TRAER TURNO POR INDICE
    public Turno getTurnoDisponiblePorIndice(int indice) {
        int contador = 0;
        for (Turno t : agenda) {
            if (t.getIdPaciente() == null && !t.getDia().isBefore(LocalDate.now())) {
                contador++;
                if (contador == indice) {
                    return t;
                }
            }
        }
        return null;
    }

    //GETTERS Y SETTERS

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public List<Turno> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<Turno> agenda) {
        this.agenda = agenda;
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







