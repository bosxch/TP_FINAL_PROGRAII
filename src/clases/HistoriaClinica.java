package clases;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoriaClinica {

    private String idHistoriaClinica;
    private String idPaciente; // identificador generico del paciente
    private List<Turno> historialTurnos;
    private List<Receta> recetasEmitidas;
    private List<Antecedentes> antecedentesMedicos;


    //CONSTRUCTOR
    public HistoriaClinica(String idHistoriaClinica, String  idPaciente) {
        this.idHistoriaClinica = idHistoriaClinica;
        this.idPaciente = idPaciente;
        this.historialTurnos = new ArrayList<>();
        this.recetasEmitidas = new ArrayList<>();
        this.antecedentesMedicos = new ArrayList<>();

    }

    //GETTERS Y SETTERS
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

    public String  getIdPaciente() {
        return idPaciente;
    }

    public void setIdHistoriaClinica(String idHistoriaClinica) {
        this.idHistoriaClinica = idHistoriaClinica;
    }

    public void setIdPaciente(String  idPaciente) {
        this.idPaciente = idPaciente;
    }

    public void setHistorialTurnos(List<Turno> historialTurnos) {
        this.historialTurnos = historialTurnos;
    }

    public void setRecetasEmitidas(List<Receta> recetasEmitidas) {
        this.recetasEmitidas = recetasEmitidas;
    }

    public void setAntecedentesMedicos(List<Antecedentes> antecedentesMedicos) {
        this.antecedentesMedicos = antecedentesMedicos;
    }

    //METODOS
    public void agregarTurno(Turno turno) {
        this.historialTurnos.add(turno);
    }
    public void agregarReceta(Receta receta) {
        this.recetasEmitidas.add(receta);
    }
    public void agregarAntecedente(Antecedentes antecedente) {
        this.antecedentesMedicos.add(antecedente);
    }
    public void eliminarTurno(Turno turno) {
        historialTurnos.remove(turno);
    }

    @Override
    public String toString() {
        return "HistoriaClinica{" +
                "idHistoriaClinica='" + idHistoriaClinica + '\'' +
                ", idPaciente=" + idPaciente +
                ", historialTurnos=" + historialTurnos +
                ", recetasEmitidas=" + recetasEmitidas +
                ", antecedentesMedicos=" + antecedentesMedicos +
                '}';
    }
}