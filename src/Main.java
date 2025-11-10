import ManejoJSON.GestorEmpleadosJson;
import ManejoJSON.GestorPacientesJson;
import clases.Empleado;
import clases.Persona;
import clases.Profesional;

public class Main {
    public static void main(String[] args)
    {
        GestorEmpleadosJson gestor = new GestorEmpleadosJson("empleados.json");
        gestor.cargarEmpleadoDesdeJson();


        gestor.agregarEmpleado("Profesional", "101", "Juan", "Pérez", "Argentina",
                "San Martín", 123, "A", "Buenos Aires", "Buenos Aires",
                "juan@correo.com", "1234", "1985-05-10",
                "E001", "M123", "CARDIOLOGIA", null);

        gestor.listarEmpleados();

        GestorPacientesJson gestor1 = new GestorPacientesJson("pacientes.json");
        gestor1.cargarPacienteDesdeJson();
        gestor1.listarPacientes();




    }
}