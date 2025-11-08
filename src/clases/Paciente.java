package clases;


import enums.Especialidad;
import enums.ObraSocial;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Paciente extends Persona {

    private String nroAfiliado;
    private ObraSocial obraSocial; // Usa el enum que implementaste
    private HistoriaClinica<String> historiaClinica; // Usa la clase genérica, con String como tipo para el ID

    public Paciente(String dni, String nombre, String apellido, String nacionalidad, Direccion direccion, String correoElectronico, String contrasenia, LocalDate fechaNacimiento, String nroAfiliado, ObraSocial obraSocial) {
        super(dni, nombre, apellido, nacionalidad, direccion, correoElectronico, contrasenia, fechaNacimiento);
        this.nroAfiliado = nroAfiliado;
        this.obraSocial = obraSocial;
        this.historiaClinica = new HistoriaClinica<>("HC-" + dni, dni);
    }

    public Paciente() {
        super();
        this.historiaClinica = new HistoriaClinica<>("HC-TEMP", "TEMP");
    }

    public String getNroAfiliado() {
        return nroAfiliado;
    }
    public ObraSocial getObraSocial() {
        return obraSocial;
    }
    public HistoriaClinica<String> getHistoriaClinica() {
        return historiaClinica;
    }

    public void setNroAfiliado(String nroAfiliado) {
        this.nroAfiliado = nroAfiliado;
    }
    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }
    public void setHistoriaClinica(HistoriaClinica<String> historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Afiliado: " + nroAfiliado +
                " | Obra Social: " + obraSocial;
    }

    //METODO PARA SACAR TURNO
    public void sacarTurno(List<Profesional> profesionales)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("ESPECIALIDADES:");
        int nro = 1;
        for (Especialidad e : Especialidad.values())
        {
            System.out.println(nro + " - " + e);
            nro++;
        }
        System.out.println("Seleccione la especialidad (ingrese el numero)");
        int eleccion = scanner.nextInt();
        scanner.nextLine();

        if(eleccion < 1 || eleccion > Especialidad.values().length)
        {
            System.out.println("La opcion ingresada es invalida");
            return;
        }
        Especialidad especialidadElegida = Especialidad.values()[eleccion - 1];

        List<Profesional> disponibles = new ArrayList<>();
        for(Profesional p : profesionales)
        {
            if (p.getEspecialidad() == especialidadElegida)
            {
                disponibles.add(p);
            }
        }

        if(disponibles.isEmpty())
        {
            System.out.println("No hay profesionales disponibles en la especialidad ingresada.");
            return;
        }

        System.out.println("PROFESIONALES DISPONIBLES EN: " + especialidadElegida);
        for(int j=0;j< disponibles.size(); j++)
        {
            Profesional p = disponibles.get(j);
            System.out.println((j+1) + " - Doc. " + p.getApellido() + " - " + p.getNombre());
        }
        System.out.println("Seleccione el profesional (ingrese el numero)");
        int profElegido = scanner.nextInt();
        scanner.nextLine();

        if(profElegido < 1 || profElegido > disponibles.size())
        {
            System.out.println("Profesion invalido.");
            return;
        }

        Profesional profesionalSeleccionado = disponibles.get(profElegido - 1);

        System.out.println("TURNOS DISPONIBLES CON DR. " + profesionalSeleccionado.getApellido() + profesionalSeleccionado.getNombre());
        int turnosDisponibles = profesionalSeleccionado.mostrarDisponibilidad();

        System.out.println("Seleccione el turno (ingrese el nro)");
        int turnoElegido = scanner.nextInt();
        scanner.nextLine();

        if (turnoElegido < 1 || turnoElegido > turnosDisponibles) {
            System.out.println("Turno inválido.");
            return;
        }

        Turno turnoSeleccionado = profesionalSeleccionado.getTurnoDisponiblePorIndice(turnoElegido - 1);

        turnoSeleccionado.setIdPaciente(this.getDni());
        historiaClinica.agregarTurno(turnoSeleccionado);

        System.out.println("TURNO RESERVADO: " + turnoSeleccionado.getDia() + " - " + turnoSeleccionado.getHora() + " Profesional: Dr. " + profesionalSeleccionado.getApellido());

    }
}