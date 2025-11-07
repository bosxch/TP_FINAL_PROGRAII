package clases;


import enums.ObraSocial;

import java.time.LocalDate;

public class Paciente extends Persona {

    private String nroAfiliado;
    private ObraSocial obraSocial; // Usa el enum que implementaste
    private HistoriaClinica<String> historiaClinica; // Usa la clase genérica, con String como tipo para el ID

    public Paciente(String dni, String nombre, String apellido, String nacionalidad, Direccion direccion, String correoElectronico, String contrasenia, LocalDate fechaNacimiento, String nroAfiliado, ObraSocial obraSocial) {
        super(dni, nombre, apellido, nacionalidad, direccion, correoElectronico, contrasenia, fechaNacimiento);
        this.nroAfiliado = nroAfiliado;
        this.obraSocial = obraSocial;
        // Se inicializa la Historia Clinica al crear el Paciente, usando el DNI como ID temporal
        this.historiaClinica = new HistoriaClinica<>("HC-" + dni, dni);
    }

    public Paciente() {
        super();
        this.historiaClinica = new HistoriaClinica<>("HC-TEMP", "TEMP");
    }

    public void agregarAntecedente(Antecedentes antecedente) {
        if (historiaClinica != null) {
            historiaClinica.agregarAntecedente(antecedente);
        }
    }

    public void agregarReceta(Receta receta) {
        if (historiaClinica != null) {
            historiaClinica.agregarReceta(receta);
        }
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
}