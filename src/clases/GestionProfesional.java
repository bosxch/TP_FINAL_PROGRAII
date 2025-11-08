package clases;

import java.util.ArrayList;
import java.util.List;

public class GestionProfesional<T extends Profesional> {

    private List<T> profesionales = new ArrayList<>();

    public GestionProfesional() {
    }

    public GestionProfesional(List<T> profesionales) {
        this.profesionales = profesionales;
    }

    public List<T> getProfesionales() {
        return profesionales;
    }

    public void setProfesionales(List<T> profesionales) {
        this.profesionales = profesionales;
    }

    public List<T> listarProfesionales(String especialidad) {
        List<T> listaProfesionales = new ArrayList<>();
        for (T prof : profesionales) {
            if (prof.getEspecialidad().equals(especialidad)) {
                listaProfesionales.add(prof);
            }
        }
        return listaProfesionales;
    }
}

