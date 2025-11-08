import clases.Empleado;
import clases.Paciente;
import clases.Persona;
import clases.Profesional;
import clases.bajarDesdeJson;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args)
    {
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

        System.out.println("\n=== FIN DE LA PRUEBA ===");
    }
}