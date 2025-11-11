package clases;

import ManejoJSON.GestorEmpleadosJson;
import ManejoJSON.GestorPacientesJson;
import enums.Especialidad;
import enums.ObraSocial;
import interfaces.IConsultarHistoriaClinica;
import java.util.Scanner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Paciente extends Persona implements IConsultarHistoriaClinica {
    Scanner sc = new Scanner(System.in);

    private String nroAfiliado;
    private ObraSocial obraSocial;
    private HistoriaClinica historiaClinica;

    // CONSTRUCTORES
    public Paciente(String dni, String nombre, String apellido, String nacionalidad, Direccion direccion, String correoElectronico, String contrasenia, LocalDate fechaNacimiento, String nroAfiliado, ObraSocial obraSocial, HistoriaClinica historiaClinica) {
        super(dni, nombre, apellido, nacionalidad, direccion, correoElectronico, contrasenia, fechaNacimiento);
        this.nroAfiliado = nroAfiliado;
        this.obraSocial = obraSocial;
        this.historiaClinica = historiaClinica;
    }
    public Paciente(String dni, String nombre, String apellido, String nacionalidad, Direccion direccion, String correoElectronico, String contrasenia, LocalDate fechaNacimiento, String nroAfiliado, ObraSocial obraSocial) {
        super(dni, nombre, apellido, nacionalidad, direccion, correoElectronico, contrasenia, fechaNacimiento);
        this.nroAfiliado = nroAfiliado;
        this.obraSocial = obraSocial;
        // Inicializa la HC para evitar NullPointerException antes de que se use el setter
        this.historiaClinica = new HistoriaClinica("HC-" + dni, dni);
    }

    public Paciente() {
        super();
        this.historiaClinica = new HistoriaClinica("HC-TEMP", "TEMP");
    }

    //GETTERS Y SETTERS
    public String getNroAfiliado() {
        return nroAfiliado;
    }

    public void setNroAfiliado(String nroAfiliado) {
        this.nroAfiliado = nroAfiliado;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }

    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    @Override
    public String getTipo() {
        return "Paciente";
    }

    //METODO PARA CONSULTAR HISTORIAL DE TURNOS
    @Override
    public List<Turno> consultarHistorialTurnos(String dniPaciente) {
        if (historiaClinica.getIdPaciente().equals(dniPaciente)) {
            return historiaClinica.getHistorialTurnos();
        }
        return new ArrayList<>();
    }

    //METODO PARA CONSULTAR RECETAS
    @Override
    public List<Receta> consultarRecetas(String dniPaciente) {
        // Validar que el DNI coincida con el paciente actual
        if (!this.getDni().equals(dniPaciente)) {
            throw new excepciones.HistoriaClinicaNoEncontradaException(
                "El DNI proporcionado no coincide con el paciente actual."
            );
        }
        
        // Validar que la historia clínica exista
        if (historiaClinica == null) {
            throw new excepciones.HistoriaClinicaNoEncontradaException(
                "No se encontró la historia clínica para el paciente con DNI " + dniPaciente
            );
        }
        
        // Obtener las recetas
        List<Receta> recetas = historiaClinica.getRecetasEmitidas();
        
        // Validar que la lista de recetas no sea null o vacía
        if (recetas == null || recetas.isEmpty()) {
            throw new excepciones.RecetasNoDisponiblesException(
                "El paciente con DNI " + dniPaciente + " no tiene recetas emitidas."
            );
        }
        
        return recetas;
    }
    
    //METODO PARA MOSTRAR RECETAS CON VALIDACIÓN
    public void mostrarRecetas() {
        try {
            List<Receta> recetas = consultarRecetas(this.getDni());
            System.out.println("\n=== RECETAS EMITIDAS ===");
            System.out.println("Paciente: " + this.getNombre() + " " + this.getApellido() + " (DNI: " + this.getDni() + ")");
            System.out.println("Total de recetas: " + recetas.size());
            System.out.println("------------------------");
            for (int i = 0; i < recetas.size(); i++) {
                System.out.println((i + 1) + ". " + recetas.get(i));
            }
        } catch (excepciones.RecetasNoDisponiblesException e) {
            System.out.println("\n⚠️ " + e.getMessage());
        } catch (excepciones.HistoriaClinicaNoEncontradaException e) {
            System.out.println("\n❌ " + e.getMessage());
        }
    }

    //METODO PARA SACAR TURNO (CORREGIDO)
    public void sacarTurno(List<Profesional> profesionales, GestorPacientesJson gestorPacientes, GestorEmpleadosJson gestorEmpleados)
    {
        System.out.println("\n--- SOLICITAR TURNO ---");
        System.out.println("ESPECIALIDADES:");
        int nro = 1;
        for (Especialidad e : Especialidad.values())
        {
            System.out.println(nro + " - " + e.name());
            nro++;
        }
        System.out.println("Seleccione la especialidad (ingrese el numero)");

        int eleccion;
        try {
            eleccion = sc.nextInt();
            sc.nextLine(); // Consumir salto de linea
        } catch (java.util.InputMismatchException e) {
            System.out.println("Entrada invalida. Debe ingresar un numero.");
            sc.nextLine();
            return;
        }


        if(eleccion < 1 || eleccion > Especialidad.values().length)
        {
            System.out.println("La opcion ingresada es invalida");
            return;
        }

        Especialidad especialidadElegida = Especialidad.values()[eleccion - 1];
        // Obtener el nombre del enum como String para la comparacion
        String nombreEspecialidad = especialidadElegida.name().toUpperCase(Locale.ROOT);


        List<Profesional> disponibles = new ArrayList<>();
        for(Profesional p : profesionales)
        {
            // Comparar la especialidad (String) del Profesional con el nombre del Enum (String) del usuario.
            if (p.getEspecialidad().name().equalsIgnoreCase(nombreEspecialidad))
            {
                disponibles.add(p);
            }
        }

        if(disponibles.isEmpty())
        {
            System.out.println("No hay profesionales disponibles en la especialidad: " + especialidadElegida.name());
            return;
        }

        System.out.println("PROFESIONALES DISPONIBLES EN: " + especialidadElegida.name());
        for(int j=0;j< disponibles.size(); j++)
        {
            Profesional p = disponibles.get(j);
            System.out.println((j+1) + " - Doc. " + p.getApellido() + " - " + p.getNombre());
        }
        System.out.println("Seleccione el profesional (ingrese el numero)");

        int profElegido;
        try {
            profElegido = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Entrada invalida. Debe ser un numero.");
            sc.nextLine();
            return;
        }


        if(profElegido < 1 || profElegido > disponibles.size())
        {
            System.out.println("Profesional invalido.");
            return;
        }

        Profesional profesionalSeleccionado = disponibles.get(profElegido - 1);

        System.out.println("TURNOS DISPONIBLES CON DR. " + profesionalSeleccionado.getApellido() + " " + profesionalSeleccionado.getNombre());
        int turnosDisponibles = profesionalSeleccionado.mostrarDisponibilidad();

        if (turnosDisponibles == 0) {
            System.out.println("El profesional seleccionado no tiene turnos libres.");
            return;
        }

        System.out.println("Seleccione el turno (ingrese el nro)");

        int turnoElegido;
        try {
            turnoElegido = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Entrada invalida. Debe ser un numero.");
            sc.nextLine();
            return;
        }


        if (turnoElegido < 1 || turnoElegido > turnosDisponibles) {
            System.out.println("Turno invalido.");
            return;
        }

        Turno turnoSeleccionado = profesionalSeleccionado.getTurnoDisponiblePorIndice(turnoElegido - 1);

        turnoSeleccionado.setIdPaciente(this.getDni());
        historiaClinica.agregarTurno(turnoSeleccionado);
        profesionalSeleccionado.agregarTurno(turnoSeleccionado);

        System.out.println("TURNO RESERVADO: " + turnoSeleccionado.getDia() + " - " + turnoSeleccionado.getHora() + " Profesional: Dr. " + profesionalSeleccionado.getApellido());

        gestorEmpleados.sincronizarProfesionalConJson(profesionalSeleccionado);
        gestorPacientes.sincronizarPacienteConJson(this);

        gestorPacientes.guardarPacientes();
        gestorEmpleados.guardarEmpleados();


        System.out.println("Turno guardado correctamente en los archivos JSON.");
    }

    public void mostrarMisTurnos(boolean numerar, List<Profesional> listaProfesionales) {
        List<Turno> turnos = historiaClinica.getHistorialTurnos();

        if (turnos.isEmpty()) {
            System.out.println("No tiene turnos registrados.");
            return;
        }

        System.out.println("=== Mis Turnos ===");
        int index = 1;

        for (Turno turno : turnos) {
            // Buscar el profesional correspondiente
            Profesional profesional = listaProfesionales.stream()
                    .filter(p -> p.getIdProfesional().equals(turno.getIdProfesional()))
                    .findFirst()
                    .orElse(null);

            String nombreProfesional = (profesional != null)
                    ? profesional.getNombre() + " " + profesional.getApellido()
                    : "Profesional no encontrado";

            if (numerar) {
                System.out.println(index + ". " +
                        turno.getDia() + " " + turno.getHora() +
                        " - Profesional: " + nombreProfesional);
                index++;
            } else {
                System.out.println(
                        turno.getDia() + " " + turno.getHora() +
                                " - Profesional: " + nombreProfesional);
            }
        }
    }

    public void cancelarTurno(int indiceTurno, List<Profesional> profesionales) {
        List<Turno> turnos = historiaClinica.getHistorialTurnos();

        if (turnos.isEmpty()) {
            System.out.println("No tienes turnos para cancelar.");
            return;
        }

        if (indiceTurno < 0 || indiceTurno >= turnos.size()) {
            System.out.println("Número de turno inválido.");
            return;
        }

        Turno turnoACancelar = turnos.get(indiceTurno);
        Profesional profesional = buscarProfesionalPorTurno(profesionales, turnoACancelar);

        if (profesional != null) {
            profesional.getTurnosDisponibles().add(turnoACancelar);
            turnos.remove(turnoACancelar);
            System.out.println("Turno cancelado correctamente.");
        } else {
            System.out.println("No se encontró el profesional del turno.");
        }
    }

    private Profesional buscarProfesionalPorTurno(List<Profesional> listaProfesionales, Turno turno) {
        for (Profesional profesional : listaProfesionales) {
            // Coincide por matrícula (idProfesional del turno)
            if (profesional.getMatricula().equals(turno.getIdProfesional())) {
                // Confirmamos que el profesional tiene ese turno (por fecha y hora)
                for (Turno t : profesional.getTurnos()) {
                    if (t.getDia().equals(turno.getDia()) &&
                            t.getHora().equals(turno.getHora())) {
                        return profesional;
                    }
                }
            }
        }
        return null; // No se encontró el profesional correspondiente
    }


}