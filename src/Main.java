import clases.Empleado;
import clases.Paciente;
import clases.Persona;
import clases.Profesional;
import clases.Administrativo;
import clases.bajarDesdeJson;
import excepciones.CredencialesIncorrectasException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Persona> usuarios = new ArrayList<>();
        List<Paciente> pacientes = new ArrayList<>();
        List<Empleado> empleados = new ArrayList<>();
        List<Profesional> profesionales = new ArrayList<>();

        // --- Creamos el manejador de carga ---
        bajarDesdeJson manejador = new bajarDesdeJson(usuarios, pacientes, empleados, profesionales);

        // --- Cargar desde archivos JSON ---
        manejador.cargarPacienteDesdeJson("pacientes.json");
        manejador.cargarEmpleadoDesdeJson("empleados.json");

        // --- Mostrar resultados ---
        manejador.mostrarPacientes();
        manejador.mostrarEmpleados();

        System.out.println("\n=== INICIO DE SESIÓN ===");

        Scanner sc = new Scanner(System.in);
        System.out.print("Correo: ");
        String email = sc.nextLine();
        System.out.print("Contraseña: ");
        String contrasenia = sc.nextLine();

        Persona personaLogueada = null;

        // --- Intentamos loguear al usuario ---
        for (Persona usuario : usuarios) {
            try {
                if (usuario.login(email, contrasenia)) {
                    personaLogueada = usuario;
                    break;
                }
            } catch (CredencialesIncorrectasException e) {
                // ignoramos excepciones hasta encontrar uno válido
            }
        }

        if (personaLogueada != null) {
            System.out.println("\nBienvenido, " + personaLogueada.getNombre() + " (" + personaLogueada.getTipo() + ")");

            // --- Ejemplo de acceso a métodos específicos según el tipo ---
            if (personaLogueada instanceof Paciente p) {
                System.out.println("Obra social: " + p.getObraSocial());
                System.out.println("Historia clínica: " + p.getHistoriaClinica().getIdHistoriaClinica());
            }
            else if (personaLogueada instanceof Profesional prof) {
                System.out.println("Especialidad: " + prof.getEspecialidad());
                System.out.println("Cantidad de turnos en agenda: " + prof.getAgenda().size());
            }
            else if (personaLogueada instanceof Administrativo adm) {
                System.out.println("Sector: " + adm.getSector());
            }

        } else {
            System.out.println("Credenciales incorrectas o usuario no encontrado.");
        }

        System.out.println("\n=== FIN DEL PROGRAMA ===");
    }
}




