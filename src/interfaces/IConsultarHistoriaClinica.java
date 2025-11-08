package interfaces;

import clases.Receta;
import clases.Antecedentes;
import clases.Turno;

import java.util.List;

// Define los metodos para consultar la información médica de un paciente
public interface IConsultarHistoriaClinica {

    List<Turno> consultarHistorialTurnos(String dniPaciente);
    List<Receta> consultarRecetas(String dniPaciente);

}