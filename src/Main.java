import ManejoJSON.GestorEmpleadosJson;
import ManejoJSON.GestorPacientesJson;
import clases.*;
import excepciones.CredencialesIncorrectasException;
import excepciones.DatoInvalidoException;
import enums.Especialidad;
import enums.ObraSocial;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        GestorEmpleadosJson gestorEmpleados = new GestorEmpleadosJson("empleados.json");
        GestorPacientesJson gestorPacientes = new GestorPacientesJson("pacientes.json");

        gestorEmpleados.cargarEmpleadoDesdeJson();
        gestorPacientes.cargarPacienteDesdeJson();

        Scanner sc = new Scanner(System.in);
        Persona personaLogueada = null;

        try {
            System.out.println("\n=== INICIO DE SESION ===");
            System.out.print("Correo: ");
            String email = sc.nextLine();
            System.out.print("Contrasenia: ");
            String contrasenia = sc.nextLine();

            personaLogueada = intentarLogin(email, contrasenia, gestorEmpleados, gestorPacientes);

            if (personaLogueada == null) {
                throw new CredencialesIncorrectasException("Datos ingresados incorrectos o usuario no encontrado.");
            }

            System.out.println("\nBienvenido, " + personaLogueada.getNombre() + " (" + personaLogueada.getTipo() + ")");

        } catch (CredencialesIncorrectasException e) {
            System.out.println(e.getMessage());
            sc.close();
            return;
        } catch (Exception e) {
            System.err.println("Ocurrio un error inesperado al intentar iniciar sesion.");
            sc.close();
            return;
        }

        boolean salir = false;
        while (!salir) {
            try {
                // MENU PACIENTE
                if (personaLogueada instanceof Paciente paciente) {
                    System.out.println("\n--- MENU PACIENTE ---");
                    System.out.println("1. Sacar turno");
                    System.out.println("2. Ver historia clinica");
                    System.out.println("3. Ver recetas emitidas");
                    System.out.println("4. Salir");
                    System.out.print("Opcion: ");
                    int op = leerOpcion(sc);

                    switch (op) {
                        case 1 ->
                            paciente.sacarTurno(gestorEmpleados.getListaProfesionales(), gestorPacientes, gestorEmpleados);


                        case 2 -> System.out.println(paciente.getHistoriaClinica());
                        case 3 -> paciente.getHistoriaClinica().getRecetasEmitidas().forEach(System.out::println);
                        case 4 -> salir = true;
                        default -> System.out.println("Opcion invalida.");
                    }
                }

                // MENU PROFESIONAL
                else if (personaLogueada instanceof Profesional profesional) {
                    System.out.println("\n--- MENU PROFESIONAL ---");
                    System.out.println("1. Consultar agenda completa");
                    System.out.println("2. Ver turnos disponibles");
                    System.out.println("3. Ver turnos ocupados");
                    System.out.println("4. Agregar receta a paciente");
                    System.out.println("5. Agregar antecedente a paciente");
                    System.out.println("6. Salir");
                    System.out.print("Opcion: ");
                    int op = leerOpcion(sc);

                    switch (op) {
                        case 1 -> profesional.consultarAgenda();
                        case 2 -> profesional.mostrarDisponibilidad();
                        case 3 -> profesional.mostrarTurnosOcupados();
                        case 4 -> {
                            System.out.print("DNI del paciente: ");
                            String dni = sc.nextLine();
                            Paciente pac = gestorPacientes.buscarPacientePorDni(dni);
                            if (pac != null) {
                                System.out.print("Diagnostico: ");
                                String diag = sc.nextLine();
                                System.out.print("Medicamento: ");
                                String med = sc.nextLine();
                                System.out.print("Dosis: ");
                                String dosis = sc.nextLine();
                                Receta receta = new Receta(UUID.randomUUID().toString(), diag, med, dosis);
                                profesional.agregarRecetaAHistoria(pac.getHistoriaClinica(), receta, gestorPacientes);
                                System.out.println("Receta agregada y guardada.");
                            } else System.out.println("Paciente no encontrado.");
                        }
                        case 5 -> {
                            System.out.print("DNI del paciente: ");
                            String dni = sc.nextLine();
                            Paciente pac = gestorPacientes.buscarPacientePorDni(dni);
                            if (pac != null) {
                                System.out.print("Descripcion: ");
                                String desc = sc.nextLine();
                                System.out.print("Tipo de antecedente: ");
                                String tipo = sc.nextLine();
                                Antecedentes ant = new Antecedentes(UUID.randomUUID().toString(), desc, tipo);
                                profesional.agregarAntecedenteAHistoria(pac.getHistoriaClinica(), ant, gestorPacientes);
                                System.out.println("Antecedente agregado y guardado.");
                            } else System.out.println("Paciente no encontrado.");
                        }
                        case 6 -> salir = true;
                        default -> System.out.println("Opcion invalida.");
                    }
                }

                // MENU ADMINISTRATIVO
                else if (personaLogueada instanceof Administrativo adm) {
                    System.out.println("\n--- MENU ADMINISTRATIVO ---");
                    System.out.println("1. Listar empleados");
                    System.out.println("2. Listar pacientes");
                    System.out.println("3. Dar de alta nuevo empleado/paciente");
                    System.out.println("4. Modificar o eliminar empleado/paciente");
                    System.out.println("5. Salir");
                    System.out.print("Opcion: ");
                    int op = leerOpcion(sc);

                    switch (op) {
                        case 1 -> gestorEmpleados.listarEmpleados();
                        case 2 -> gestorPacientes.listarPacientes();
                        case 3 -> {
                            System.out.println("\n¿A quien queres dar de alta?");
                            System.out.println("1. Profesional");
                            System.out.println("2. Administrativo");
                            System.out.println("3. Paciente");
                            System.out.print("Opcion: ");
                            int tipo = leerOpcion(sc);

                            switch (tipo) {
                                case 1 -> altaProfesional(gestorEmpleados, sc);
                                case 2 -> altaAdministrativo(gestorEmpleados, sc);
                                case 3 -> altaPaciente(gestorPacientes, sc);
                                default -> System.out.println("Opcion invalida.");
                            }
                        }
                        case 4 -> {
                            System.out.println("\n¿Que tipo de ususario desea modificar/eliminar?");
                            System.out.println("1. Empleado");
                            System.out.println("2. Paciente");
                            System.out.print("Opcion: ");
                            int tipo = leerOpcion(sc);
                            switch (tipo) {
                                case 1 -> {
                                    System.out.println("1. Modificar empleado");
                                    System.out.println("2. Eliminar empleado");
                                    int opEmp = leerOpcion(sc);
                                    if (opEmp == 1) modificarEmpleado(gestorEmpleados, sc);
                                    else if (opEmp == 2) eliminarEmpleado(gestorEmpleados, sc);
                                    else System.out.println("Opción inválida.");
                                }
                                case 2 -> {
                                    System.out.println("1. Modificar paciente");
                                    System.out.println("2. Eliminar paciente");
                                    int opPac = leerOpcion(sc);
                                    if (opPac == 1) modificarPaciente(gestorPacientes, sc);
                                    else if (opPac == 2) eliminarPaciente(gestorPacientes, sc);
                                    else System.out.println("Opción inválida.");
                                }
                                default -> System.out.println("Opción inválida.");
                            }
                        }

                        case 5 -> salir = true;
                        default -> System.out.println("Opcion invalida.");
                    }
                }
            } catch (DatoInvalidoException e) {
                System.out.println("Error de Dato: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Ocurrio un error inesperado en el menu: " + e.getMessage());
            }
        }

        System.out.println("\n=== FIN DEL PROGRAMA ===");
        sc.close();
    }
    // METODOS AUXILIARES Y DE LOGIN
    private static Persona intentarLogin(String email, String contrasenia, GestorEmpleadosJson gestorEmpleados, GestorPacientesJson gestorPacientes) {
        // Login en empleados
        for (Empleado emp : gestorEmpleados.getListaEmpleados()) {
            if (emp.getCorreoElectronico().equalsIgnoreCase(email) && emp.verificarContrasenia(contrasenia)) {
                return emp;
            }
        }

        // Login en pacientes
        for (Paciente pac : gestorPacientes.getListaPacientes()) {
            if (pac.getCorreoElectronico().equalsIgnoreCase(email) && pac.verificarContrasenia(contrasenia)) {
                return pac;
            }
        }
        return null;
    }


    /**
     * Lee la opcion del menu, manejando errores de InputMismatch (si no es un numero).
     */
    private static int leerOpcion(Scanner sc) {
        try {
            int op = sc.nextInt();
            sc.nextLine(); // Consumir el salto de linea
            return op;
        } catch (InputMismatchException e) {
            sc.nextLine(); // Limpiar el buffer de entrada invalida
            return -1; // Retorna un valor invalido
        }
    }

    // METODOS DE ALTA (para administrativo) con validacion de datos

    private static void altaProfesional(GestorEmpleadosJson gestor, Scanner sc) throws DatoInvalidoException {
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
        System.out.print("Numero: ");
        int numero;
        try {
            numero = sc.nextInt(); sc.nextLine();
        } catch (InputMismatchException e) {
            sc.nextLine();
            throw new DatoInvalidoException("El numero de calle debe ser un valor numerico.");
        }
        System.out.print("Depto: ");
        String depto = sc.nextLine();
        System.out.print("Ciudad: ");
        String ciudad = sc.nextLine();
        System.out.print("Provincia: ");
        String provincia = sc.nextLine();
        System.out.print("Correo: ");
        String correo = sc.nextLine();
        System.out.print("Contrasenia: ");
        String pass = sc.nextLine();
        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        String fecha = sc.nextLine();

        // Validacion de fecha
        try {
            LocalDate.parse(fecha);
        } catch (Exception e) {
            throw new DatoInvalidoException("Formato de fecha de nacimiento invalido. Use YYYY-MM-DD.");
        }

        System.out.print("Legajo: ");
        String legajo = sc.nextLine();
        System.out.print("Matricula: ");
        String matricula = sc.nextLine();
        System.out.print("Especialidad (CARDIOLOGIA, PEDIATRIA, TRAUMATOLOGIA, etc.): ");
        String espStr = sc.nextLine().toUpperCase();

        // Validacion de Enum
        try {
            Especialidad.valueOf(espStr);
        } catch (IllegalArgumentException e) {
            throw new DatoInvalidoException("La especialidad ingresada no es valida.");
        }

        gestor.agregarEmpleado("Profesional", dni, nombre, apellido, nac, calle, numero, depto, ciudad, provincia,
                correo, pass, fecha, legajo, matricula, espStr, null);
        gestor.cargarEmpleadoDesdeJson(); // Recargar para que este disponible inmediatamente en la lista
        System.out.println("Profesional agregado y guardado en empleados.json");
    }

    private static void altaAdministrativo(GestorEmpleadosJson gestor, Scanner sc) throws DatoInvalidoException {
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
        System.out.print("Numero: ");
        int numero;
        try {
            numero = sc.nextInt(); sc.nextLine();
        } catch (InputMismatchException e) {
            sc.nextLine();
            throw new DatoInvalidoException("El numero de calle debe ser un valor numerico.");
        }
        System.out.print("Depto: ");
        String depto = sc.nextLine();
        System.out.print("Ciudad: ");
        String ciudad = sc.nextLine();
        System.out.print("Provincia: ");
        String provincia = sc.nextLine();
        System.out.print("Correo: ");
        String correo = sc.nextLine();
        System.out.print("Contrasenia: ");
        String pass = sc.nextLine();
        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        String fecha = sc.nextLine();

        // Validacion de fecha
        try {
            LocalDate.parse(fecha);
        } catch (Exception e) {
            throw new DatoInvalidoException("Formato de fecha de nacimiento invalido. Use YYYY-MM-DD.");
        }

        System.out.print("Legajo: ");
        String legajo = sc.nextLine();
        System.out.print("Sector: ");
        String sector = sc.nextLine();

        gestor.agregarEmpleado("Administrativo", dni, nombre, apellido, nac, calle, numero, depto, ciudad, provincia,
                correo, pass, fecha, legajo, null, null, sector);
        gestor.cargarEmpleadoDesdeJson();
        System.out.println("Administrativo agregado y guardado en empleados.json");
    }

    private static void altaPaciente(GestorPacientesJson gestor, Scanner sc) throws DatoInvalidoException {
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
        System.out.print("Numero: ");
        int numero;
        try {
            numero = sc.nextInt(); sc.nextLine();
        } catch (InputMismatchException e) {
            sc.nextLine();
            throw new DatoInvalidoException("El numero de calle debe ser un valor numerico.");
        }
        System.out.print("Depto: ");
        String depto = sc.nextLine();
        System.out.print("Ciudad: ");
        String ciudad = sc.nextLine();
        System.out.print("Provincia: ");
        String provincia = sc.nextLine();
        System.out.print("Correo: ");
        String correo = sc.nextLine();
        System.out.print("Contrasenia: ");
        String pass = sc.nextLine();
        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        String fecha = sc.nextLine();

        // Validacion de fecha
        try {
            LocalDate.parse(fecha);
        } catch (Exception e) {
            throw new DatoInvalidoException("Formato de fecha de nacimiento invalido. Use YYYY-MM-DD.");
        }

        System.out.print("N Afiliado: ");
        String afiliado = sc.nextLine();
        System.out.print("Obra Social (EJ: PAMI, OSDE, NINGUNA): ");
        String obraStr = sc.nextLine().toUpperCase();

        // Validacion de Enum
        try {
            ObraSocial.valueOf(obraStr);
        } catch (IllegalArgumentException e) {
            throw new DatoInvalidoException("La Obra Social ingresada no es valida.");
        }

        gestor.agregarPaciente(dni, nombre, apellido, nac, calle, numero, depto, ciudad, provincia,
                correo, pass, fecha, afiliado, obraStr);
        gestor.cargarPacienteDesdeJson();
        System.out.println("Paciente agregado y guardado en pacientes.json");
    }

    //METODOS PARA MODIFICAR Y ELIMINAR EMPLEADOS Y PACIENTES
    private static void modificarPaciente(GestorPacientesJson gestor, Scanner sc) {
        System.out.print("Ingrese el DNI del paciente a modificar: ");
        String dni = sc.nextLine();
        JSONObject paciente = gestor.obtenerPaciente(dni);
        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        try {
            System.out.print("Nuevo nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Nuevo apellido: ");
            String apellido = sc.nextLine();
            System.out.print("Nueva nacionalidad: ");
            String nac = sc.nextLine();
            System.out.print("Nueva calle: ");
            String calle = sc.nextLine();
            System.out.print("Nuevo número: ");
            int numero = sc.nextInt(); sc.nextLine();
            System.out.print("Nuevo depto: ");
            String depto = sc.nextLine();
            System.out.print("Nueva ciudad: ");
            String ciudad = sc.nextLine();
            System.out.print("Nueva provincia: ");
            String provincia = sc.nextLine();
            System.out.print("Nuevo correo: ");
            String correo = sc.nextLine();
            System.out.print("Nueva contraseña: ");
            String pass = sc.nextLine();
            System.out.print("Nueva fecha de nacimiento (YYYY-MM-DD): ");
            String fecha = sc.nextLine();
            System.out.print("Nuevo número de afiliado: ");
            String afiliado = sc.nextLine();
            System.out.print("Nueva obra social: ");
            String obra = sc.nextLine().toUpperCase();

            gestor.modificarPaciente(dni, nombre, apellido, nac, calle, numero, depto, ciudad,
                    provincia, correo, pass, fecha, afiliado, obra);
            gestor.cargarPacienteDesdeJson();
            System.out.println("✅ Paciente modificado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al modificar paciente: " + e.getMessage());
        }
    }

    private static void eliminarPaciente(GestorPacientesJson gestor, Scanner sc) {
        System.out.print("Ingrese el DNI del paciente a eliminar: ");
        String dni = sc.nextLine();
        JSONObject paciente = gestor.obtenerPaciente(dni);
        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }
        gestor.eliminarPaciente(dni);
        gestor.cargarPacienteDesdeJson();
        System.out.println("🗑️ Paciente eliminado correctamente.");
    }

    private static void modificarEmpleado(GestorEmpleadosJson gestor, Scanner sc) {
        System.out.print("Ingrese el DNI del empleado a modificar: ");
        String dni = sc.nextLine();
        JSONObject empleado = gestor.obtenerEmpleado(dni);
        if (empleado == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        try {
            System.out.print("Ingrese el DNI del empleado a modificar: ");

            System.out.print("Nuevo nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Nuevo apellido: ");
            String apellido = sc.nextLine();
            System.out.print("Nueva nacionalidad: ");
            String nac = sc.nextLine();
            System.out.print("Nueva calle: ");
            String calle = sc.nextLine();
            System.out.print("Nuevo número: ");
            int numero = sc.nextInt(); sc.nextLine();
            System.out.print("Nuevo depto: ");
            String depto = sc.nextLine();
            System.out.print("Nueva ciudad: ");
            String ciudad = sc.nextLine();
            System.out.print("Nueva provincia: ");
            String provincia = sc.nextLine();
            System.out.print("Nuevo correo: ");
            String correo = sc.nextLine();
            System.out.print("Nueva contraseña: ");
            String pass = sc.nextLine();
            System.out.print("Nueva fecha de nacimiento (YYYY-MM-DD): ");
            String fecha = sc.nextLine();

            System.out.print("Nuevo legajo: ");
            String legajo = sc.nextLine();

            System.out.print("¿El empleado es profesional o administrativo? ");
            String tipo = sc.nextLine().trim().toLowerCase();

            String matricula = null;
            String especialidad = null;
            String sector = null;

            if (tipo.equals("profesional")) {
                System.out.print("Nueva matrícula: ");
                matricula = sc.nextLine();
                System.out.print("Nueva especialidad: ");
                especialidad = sc.nextLine();
            } else {
                System.out.print("Nuevo sector: ");
                sector = sc.nextLine();
            }

            gestor.modificarEmpleado(
                    dni, nombre, apellido, nac, calle, numero, depto, ciudad, provincia,
                    correo, pass, fecha, legajo, matricula, especialidad, sector
            );
            gestor.cargarEmpleadoDesdeJson();
            System.out.println("✅ Empleado modificado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al modificar empleado: " + e.getMessage());
        }
    }

    private static void eliminarEmpleado(GestorEmpleadosJson gestor, Scanner sc) {
        System.out.print("Ingrese el DNI del empleado a eliminar: ");
        String dni = sc.nextLine();
        JSONObject empleado = gestor.obtenerEmpleado(dni);
        if (empleado == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }
        gestor.eliminarEmpleado(dni);
        gestor.cargarEmpleadoDesdeJson();
        System.out.println("🗑️ Empleado eliminado correctamente.");
    }
}