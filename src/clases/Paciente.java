package clases;


import enums.ObraSocial;
import interfaces.IConsultarHistoriaClinica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente extends Persona implements IConsultarHistoriaClinica {

    private String nroAfiliado;
    private ObraSocial obraSocial; // Usa el enum que implementaste
    private HistoriaClinica historiaClinica; // Usa la clase genérica, con String como tipo para el ID

    public Paciente(String dni, String nombre, String apellido, String nacionalidad, Direccion direccion, String correoElectronico, String contrasenia, LocalDate fechaNacimiento, String nroAfiliado, ObraSocial obraSocial) {
        super(dni, nombre, apellido, nacionalidad, direccion, correoElectronico, contrasenia, fechaNacimiento);
        this.nroAfiliado = nroAfiliado;
        this.obraSocial = obraSocial;
        // Se inicializa la Historia Clinica al crear el Paciente, usando el DNI como ID temporal
        this.historiaClinica = new HistoriaClinica("HC-" + dni, dni);
    }

    public Paciente() {
        super();
        this.historiaClinica = new HistoriaClinica("HC-TEMP", "TEMP");
    }

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
    public List<Turno> consultarHistorialTurnos(String dniPaciente) {
        if (historiaClinica.getIdPaciente().equals(dniPaciente)) {
            return historiaClinica.getHistorialTurnos();
        }
        return new ArrayList<>();
    }

    public List<Receta> consultarRecetas(String dniPaciente) {
        if (historiaClinica.getIdPaciente().equals(dniPaciente)) {
            return historiaClinica.getRecetasEmitidas();
        }
        return new ArrayList<>();
    }

    //agregar sacar y eliminar turno

    @Override
    public String toString() {
        return super.toString() +
                " | Afiliado: " + nroAfiliado +
                " | Obra Social: " + obraSocial;
    }
}