package clases;

import java.util.ArrayList;
import java.util.List;

public class HistoriaClinica<T> {

    private String idHistoriaClinica;
    private T idPaciente; // Usamos T para un identificador generico del paciente

    private List<Turno> historialTurnos;
    private List<Receta> recetasEmitidas;
    private List<Antecedentes> antecedentesMedicos;

    public HistoriaClinica(String idHistoriaClinica, T idPaciente) {
        this.idHistoriaClinica = idHistoriaClinica;
        this.idPaciente = idPaciente;
        this.historialTurnos = new ArrayList<>();
        this.recetasEmitidas = new ArrayList<>();
        this.antecedentesMedicos = new ArrayList<>();
    }


    public void agregarTurno(Turno turno) {
        this.historialTurnos.add(turno);
    }

    public void agregarReceta(Receta receta) {
        this.recetasEmitidas.add(receta);
    }

    public void agregarAntecedente(Antecedentes antecedente) {
        this.antecedentesMedicos.add(antecedente);
    }

    public String getIdHistoriaClinica() {
        return idHistoriaClinica;
    }
    public List<Turno> getHistorialTurnos() {
        return historialTurnos;
    }
    public List<Receta> getRecetasEmitidas() {
        return recetasEmitidas;
    }
    public List<Antecedentes> getAntecedentesMedicos() {
        return antecedentesMedicos;
    }

    @Override
    public String toString() {
        return "HistoriaClinica [ID=" + idHistoriaClinica + ", Turnos=" + historialTurnos.size() +
                ", Recetas=" + recetasEmitidas.size() + ", Antecedentes=" + antecedentesMedicos.size() + "]";
    }
}