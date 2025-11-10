package clases;

import ManejoJSON.GestorPacientesJson;
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
    private List<HistoriaClinica> todasLasHistorias;

    //CONSTRUCTOR
    public Profesional(String dni, String nombre, String apellido, String nacionalidad, Direccion direccion, String correoElectronico, String contraseña, LocalDate fechaNacimiento, String legajo, String matricula, Especialidad especialidad) {
        super(dni, nombre, apellido, nacionalidad, direccion, correoElectronico, contraseña, fechaNacimiento, legajo);
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.todasLasHistorias = new ArrayList<>();
        this.agenda = generarAgenda();
    }

    public Profesional() {
        this.agenda = new ArrayList<>();
    }

    //GETTERS Y STTERS
    public List<HistoriaClinica> getTodasLasHistorias() {
        return todasLasHistorias;
    }

    public void setTodasLasHistorias(List<HistoriaClinica> todasLasHistorias) {
        this.todasLasHistorias = todasLasHistorias;
    }
    @Override
    public String getTipo() {
        return "Profesional";
    }

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
    public void verHistoriaClinica(HistoriaClinica historia) {
        System.out.println(historia);
    }
    @Override
    public String getTipoEmpleado() {
        return "Profesional de la salud";
    }

    // CONSULTAR LA AGENDA
    public List<Turno> consultarAgenda() {
        System.out.println("Agenda del profesional " + getNombre() + " " + getApellido() + " (" + getMatricula() + "):");
        if (agenda == null || agenda.isEmpty()) {
            System.out.println("No hay turnos cargados en la agenda.");
            return new ArrayList<>();
        }

        for (Turno turno : agenda) {
            String estado = (turno.getIdPaciente() == null) ? "DISPONIBLE" : "OCUPADO - Paciente: " + turno.getIdPaciente();
            System.out.println(turno.getDia() + " " + turno.getHora() + " → " + estado);
        }

        return agenda;
    }
    // GENERA AGENDA PARA LOS PROXIMOS 30 DÌAS HÀBILES de lun a vie
    public List<Turno> generarAgenda()
    {
        List<Turno> agenda = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusMonths(1);
        String idProfesional = this.matricula;

        while (!hoy.isAfter(limite))
        {
            if (hoy.getDayOfWeek() != DayOfWeek.SATURDAY && hoy.getDayOfWeek() != DayOfWeek.SUNDAY) {
                for (int i = 8; i < 13; i++) {
                    Turno turno = new Turno(
                            UUID.randomUUID().toString(),
                            null, // paciente nulo = disponible
                            idProfesional,
                            hoy,
                            LocalTime.of(i, 0)
                    );
                    agenda.add(turno);
                }
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

        // Cambia i de contador de bucle a contador de salida
        int nroTurno = 1;
        int cantidadDisponibles = 0;

        boolean hayDisponibles = false;
        for (Turno t : agenda) {
            if (t.getIdPaciente() == null && !t.getDia().isBefore(LocalDate.now())) {
                System.out.println(nroTurno + " - " + t.getDia() + " " + t.getHora()); // Usa nroTurno aquí
                hayDisponibles = true;
                cantidadDisponibles++;
                nroTurno++; // ¡Incrementa el número de salida!
            }
        }
        if (!hayDisponibles) {
            System.out.println("No hay turnos disponibles actualmente.");
        }
        return cantidadDisponibles;
    }
    public void mostrarTurnosOcupados() {
        System.out.println("\nTurnos ocupados del profesional " + getNombre() + " " + getApellido() + ":");
        boolean hayOcupados = false;
        for (Turno t : agenda) {
            if (t.getIdPaciente() != null) {
                System.out.println("Día: " + t.getDia() + " " + t.getHora() + " → Paciente: " + t.getIdPaciente());
                hayOcupados = true;
            }
        }
        if (!hayOcupados) System.out.println("No hay turnos ocupados actualmente.");
    }

    // METODO PARA TRAER TURNO POR INDICE
    public Turno getTurnoDisponiblePorIndice(int indice) {
        // Ingresar posición base 0 (0, 1, 2)
        int contadorTurnosLibres = 0;
        for (Turno t : agenda) {
            // 1. Filtramos solo los turnos disponibles y futuros
            if (t.getIdPaciente() == null && !t.getDia().isBefore(LocalDate.now())) {

                // 2. Si el contador coincide con el índice base 0, retornamos el turno.
                if (contadorTurnosLibres == indice) {
                    return t;
                }
                // 3. Incrementamos el contador para pasar al siguiente turno libre
                contadorTurnosLibres++;
            }
        }
        // Si el contador nunca coincide (porque el índice es demasiado grande o la lista estaba vacía)
        return null;
    }
    //CONSULTAR HISTORIA CLINICA
    @Override
    public List<Turno> consultarHistorialTurnos(String dniPaciente) {
        for (HistoriaClinica historia : todasLasHistorias) {
            if (historia.getIdPaciente().equals(dniPaciente)) {
                return historia.getHistorialTurnos();
            }
        }
        return new ArrayList<>(); // si no encuentra la historia
    }

    //CONSULTAR HISTORIA RECETA
    @Override
    public List<Receta> consultarRecetas(String dniPaciente) {
        for (HistoriaClinica historia : todasLasHistorias) {
            if (historia.getIdPaciente().equals(dniPaciente)) {
                return historia.getRecetasEmitidas();
            }
        }
        return new ArrayList<>();
    }

    //AGREGAR RECETA A HC DE PACIENTE
    public void agregarRecetaAHistoria(HistoriaClinica historia, Receta receta, GestorPacientesJson gestorPacientes) {
        // 1. Agregar la receta a la Historia Clínica en memoria
        historia.agregarReceta(receta);

        // 2. Obtener el paciente para actualizar su objeto en la lista
        Paciente pacienteAActualizar = gestorPacientes.buscarPacientePorDni((String) historia.getIdPaciente());

        if (pacienteAActualizar != null) {
            // 3. Persistir el objeto Paciente completo al archivo JSON
            // El metodo actualizarPaciente internamente ya actualiza la lista en memoria del gestor
            gestorPacientes.actualizarPaciente(pacienteAActualizar);
            gestorPacientes.guardarPacientes(); // <--- ESTE ES EL PASO CRÍTICO: GUARDA EN EL ARCHIVO FÍSICO
        }
    }

    //AGREGAR ANTECEDENTE A HC DE PACIENTE
    public void agregarAntecedenteAHistoria(HistoriaClinica historia, Antecedentes antecedente, GestorPacientesJson gestorPacientes) {
        // 1. Agregar el antecedente a la Historia Clínica en memoria
        historia.agregarAntecedente(antecedente);

        // 2. Obtener el paciente para actualizar su objeto en la lista
        Paciente pacienteAActualizar = gestorPacientes.buscarPacientePorDni((String) historia.getIdPaciente());

        if (pacienteAActualizar != null) {
            // 3. Persistir el objeto Paciente completo al archivo JSON
            gestorPacientes.actualizarPaciente(pacienteAActualizar);
            gestorPacientes.guardarPacientes(); // <--- ESTE ES EL PASO CRÍTICO: GUARDA EN EL ARCHIVO FÍSICO
        }
    }

    //AGREGAR UN TURNO
    public void agregarTurno(Turno turno) {
        if (agenda == null) {
            agenda = new ArrayList<>();
        }
        for (Turno t : agenda) {
            if (t.getIdTurno().equals(turno.getIdTurno())) {
                t.setIdPaciente(turno.getIdPaciente());
                return;
            }
        }
        agenda.add(turno);
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







