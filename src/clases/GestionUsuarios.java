package clases;

import java.util.ArrayList;
import java.util.List;

public class GestionUsuarios {

    private List<Persona> usuarios = new ArrayList<>();

    public GestionUsuarios() {
    }

    public GestionUsuarios(List<Persona> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Persona> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Persona> usuarios) {
        this.usuarios = usuarios;
    }

    public void agregarUsuario(Persona usuario) {
        this.usuarios.add(usuario);
    }


    //Login centralizado: devuelve el usuario correspondiente según su tipo
    public Persona login(String email, String contrasenia) {
        for (Persona usuario : usuarios) {
            if (usuario.getCorreoElectronico().equalsIgnoreCase(email)
                    && usuario.verificarContrasenia(contrasenia)) {
                return usuario; // Devuelve el objeto del tipo correcto (Paciente, Profesional, Administrativo)
            }
        }
        return null; // no encontrado o contraseña incorrecta
    }


     //Listado específico de profesionales por especialidad

    public List<Profesional> listarProfesionales(String especialidad) {
        List<Profesional> listaProfesionales = new ArrayList<>();
        for (Persona usuario : usuarios) {
            if (usuario instanceof Profesional prof) {
                if (prof.getEspecialidad().equals(especialidad)) {
                    listaProfesionales.add(prof);
                }
            }
        }
        return listaProfesionales;
    }
}
