import ManejoJSON.GestorEmpleadosJson;
import ManejoJSON.GestorPacientesJson;
import clases.*;
import excepciones.CredencialesIncorrectasException;

import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        GestorEmpleadosJson gestorEmpleados = new GestorEmpleadosJson("empleados.json");
        GestorPacientesJson gestorPacientes = new GestorPacientesJson("pacientes.json");

        gestorEmpleados.cargarEmpleadoDesdeJson();
        gestorPacientes.cargarPacienteDesdeJson();

        System.out.println("\n=== INICIO DE SESIÓN ===");
        Scanner sc = new Scanner(System.in);

        System.out.print("Correo: ");
        String email = sc.nextLine();
        System.out.print("Contraseña: ");
        String contrasenia = sc.nextLine();

        Persona personaLogueada = null;

        // --- Login en empleados ---
        for (Empleado emp : gestorEmpleados.getListaEmpleados()) {
            if (emp.getCorreoElectronico().equalsIgnoreCase(email) && emp.verificarContrasenia(contrasenia)) {
                personaLogueada = emp;
                break;
            }
        }

        // --- Login en pacientes ---
        if (personaLogueada == null) {
            for (Paciente pac : gestorPacientes.getListaPacientes()) {
                if (pac.getCorreoElectronico().equalsIgnoreCase(email) && pac.verificarContrasenia(contrasenia)) {
                    personaLogueada = pac;
                    break;
                }
            }
        }

        if (personaLogueada == null) {
            System.out.println("Credenciales incorrectas o usuario no encontrado.");
            return;
        }

        System.out.println("\nBienvenido, " + personaLogueada.getNombre() + " (" + personaLogueada.getTipo() + ")");

        boolean salir = false;
        while (!salir) {

            // ============================
            // MENÚ PACIENTE
            // ============================
            if (personaLogueada instanceof Paciente paciente) {
                System.out.println("\n--- MENÚ PACIENTE ---");
                System.out.println("1. Sacar turno");
                System.out.println("2. Ver historia clínica");
                System.out.println("3. Ver recetas emitidas");
                System.out.println("4. Salir");
                System.out.print("Opción: ");
                int op = sc.nextInt();
                sc.nextLine();

                switch (op) {
                    case 1 -> paciente.sacarTurno(gestorEmpleados.getListaProfesionales(), gestorPacientes, gestorEmpleados);
                    case 2 -> System.out.println(paciente.getHistoriaClinica());
                    case 3 -> paciente.getHistoriaClinica().getRecetasEmitidas().forEach(System.out::println);
                    case 4 -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            }

            // ============================
            // MENÚ PROFESIONAL
            // ============================
            else if (personaLogueada instanceof Profesional profesional) {
                System.out.println("\n--- MENÚ PROFESIONAL ---");
                System.out.println("1. Consultar agenda completa");
                System.out.println("2. Ver turnos disponibles");
                System.out.println("3. Ver turnos ocupados");
                System.out.println("4. Agregar receta a paciente");
                System.out.println("5. Agregar antecedente a paciente");
                System.out.println("6. Salir");
                System.out.print("Opción: ");
                int op = sc.nextInt();
                sc.nextLine();

                switch (op) {
                    case 1 -> profesional.consultarAgenda();
                    case 2 -> profesional.mostrarDisponibilidad();
                    case 3 -> profesional.mostrarTurnosOcupados();
                    case 4 -> {
                        System.out.print("DNI del paciente: ");
                        String dni = sc.nextLine();
                        Paciente pac = gestorPacientes.buscarPacientePorDni(dni);
                        if (pac != null) {
                            System.out.print("Diagnóstico: ");
                            String diag = sc.nextLine();
                            System.out.print("Medicamento: ");
                            String med = sc.nextLine();
                            System.out.print("Dosis: ");
                            String dosis = sc.nextLine();
                            Receta receta = new Receta(UUID.randomUUID().toString(), diag, med, dosis);
                            profesional.agregarRecetaAHistoria(pac.getHistoriaClinica(), receta, gestorPacientes);
                            System.out.println("✅ Receta agregada correctamente.");
                        } else System.out.println("Paciente no encontrado.");
                    }
                    case 5 -> {
                        System.out.print("DNI del paciente: ");
                        String dni = sc.nextLine();
                        Paciente pac = gestorPacientes.buscarPacientePorDni(dni);
                        if (pac != null) {
                            System.out.print("Descripción: ");
                            String desc = sc.nextLine();
                            System.out.print("Tipo de antecedente: ");
                            String tipo = sc.nextLine();
                            Antecedentes ant = new Antecedentes(UUID.randomUUID().toString(), desc, tipo);
                            profesional.agregarAntecedenteAHistoria(pac.getHistoriaClinica(), ant, gestorPacientes);
                            System.out.println("✅ Antecedente agregado correctamente.");
                        } else System.out.println("Paciente no encontrado.");
                    }
                    case 6 -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            }

            // ============================
            // MENÚ ADMINISTRATIVO
            // ============================
            else if (personaLogueada instanceof Administrativo adm) {
                System.out.println("\n--- MENÚ ADMINISTRATIVO ---");
                System.out.println("1. Listar empleados");
                System.out.println("2. Listar pacientes");
                System.out.println("3. Dar de alta nuevo empleado/paciente");
                System.out.println("4. Salir");
                System.out.print("Opción: ");
                int op = sc.nextInt();
                sc.nextLine();

                switch (op) {
                    case 1 -> gestorEmpleados.listarEmpleados();
                    case 2 -> gestorPacientes.listarPacientes();
                    case 3 -> {
                        System.out.println("\n¿A quién querés dar de alta?");
                        System.out.println("1. Profesional");
                        System.out.println("2. Administrativo");
                        System.out.println("3. Paciente");
                        System.out.print("Opción: ");
                        int tipo = sc.nextInt();
                        sc.nextLine();

                        switch (tipo) {
                            case 1 -> altaProfesional(gestorEmpleados, sc);
                            case 2 -> altaAdministrativo(gestorEmpleados, sc);
                            case 3 -> altaPaciente(gestorPacientes, sc);
                            default -> System.out.println("Opción inválida.");
                        }
                    }
                    case 4 -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            }
        }

        System.out.println("\n=== FIN DEL PROGRAMA ===");
    }

    // ==========================================================
    // MÉTODOS DE ALTA (para administrativo)
    // ==========================================================

    private static void altaProfesional(GestorEmpleadosJson gestor, Scanner sc) {
        System.out.println("\n--- Alta de Profesional ---");
        System.out.print("DNI: ");
        String dni = sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Apellido: ");
        String apellido = sc.nextLine();
        System.out.print("Nacionalidad: ");
        String nac = sc.nextLine();
        System.out.print("Calle: ");
        String calle = sc.nextLine();
        System.out.print("Número: ");
        int numero = sc.nextInt(); sc.nextLine();
        System.out.print("Depto: ");
        String depto = sc.nextLine();
        System.out.print("Ciudad: ");
        String ciudad = sc.nextLine();
        System.out.print("Provincia: ");
        String provincia = sc.nextLine();
        System.out.print("Correo: ");
        String correo = sc.nextLine();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();
        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        String fecha = sc.nextLine();
        System.out.print("Legajo: ");
        String legajo = sc.nextLine();
        System.out.print("Matrícula: ");
        String matricula = sc.nextLine();
        System.out.print("Especialidad (CARDIOLOGIA, PEDIATRIA, TRAUMATOLOGIA, etc.): ");
        String esp = sc.nextLine();

        gestor.agregarEmpleado("Profesional", dni, nombre, apellido, nac, calle, numero, depto, ciudad, provincia,
                correo, pass, fecha, legajo, matricula, esp, null);
        System.out.println("✅ Profesional agregado y guardado en empleados.json");
    }

    private static void altaAdministrativo(GestorEmpleadosJson gestor, Scanner sc) {
        System.out.println("\n--- Alta de Administrativo ---");
        System.out.print("DNI: ");
        String dni = sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Apellido: ");
        String apellido = sc.nextLine();
        System.out.print("Nacionalidad: ");
        String nac = sc.nextLine();
        System.out.print("Calle: ");
        String calle = sc.nextLine();
        System.out.print("Número: ");
        int numero = sc.nextInt(); sc.nextLine();
        System.out.print("Depto: ");
        String depto = sc.nextLine();
        System.out.print("Ciudad: ");
        String ciudad = sc.nextLine();
        System.out.print("Provincia: ");
        String provincia = sc.nextLine();
        System.out.print("Correo: ");
        String correo = sc.nextLine();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();
        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        String fecha = sc.nextLine();
        System.out.print("Legajo: ");
        String legajo = sc.nextLine();
        System.out.print("Sector: ");
        String sector = sc.nextLine();

        gestor.agregarEmpleado("Administrativo", dni, nombre, apellido, nac, calle, numero, depto, ciudad, provincia,
                correo, pass, fecha, legajo, null, null, sector);
        System.out.println("✅ Administrativo agregado y guardado en empleados.json");
    }

    private static void altaPaciente(GestorPacientesJson gestor, Scanner sc) {
        System.out.println("\n--- Alta de Paciente ---");
        System.out.print("DNI: ");
        String dni = sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Apellido: ");
        String apellido = sc.nextLine();
        System.out.print("Nacionalidad: ");
        String nac = sc.nextLine();
        System.out.print("Calle: ");
        String calle = sc.nextLine();
        System.out.print("Número: ");
        int numero = sc.nextInt(); sc.nextLine();
        System.out.print("Depto: ");
        String depto = sc.nextLine();
        System.out.print("Ciudad: ");
        String ciudad = sc.nextLine();
        System.out.print("Provincia: ");
        String provincia = sc.nextLine();
        System.out.print("Correo: ");
        String correo = sc.nextLine();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();
        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        String fecha = sc.nextLine();
        System.out.print("N° Afiliado: ");
        String afiliado = sc.nextLine();
        System.out.print("Obra Social: ");
        String obra = sc.nextLine();

        gestor.agregarPaciente(dni, nombre, apellido, nac, calle, numero, depto, ciudad, provincia,
                correo, pass, fecha, afiliado, obra);
        System.out.println("✅ Paciente agregado y guardado en pacientes.json");
    }
}




