import ManejoJSON.GestorEmpleadosJson;
import ManejoJSON.GestorPacientesJson;
import clases.*;
import excepciones.CredencialesIncorrectasException;
import excepciones.DatoInvalidoException;
import enums.Especialidad;
import enums.ObraSocial;
import org.json.JSONObject;
import utils.InputUtils;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
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
        int maxIntentos = 3;
        int intentos = 0;

        while (personaLogueada == null && intentos < maxIntentos) {
            intentos++;
            System.out.println("\n=== INICIO DE SESION (Intento " + intentos + " de " + maxIntentos + ") ===");
            System.out.print("Correo: ");
            String email = sc.nextLine();
            System.out.print("Contrasenia: ");
            String contrasenia = sc.nextLine();

            try {
                personaLogueada = intentarLogin(email, contrasenia, gestorEmpleados, gestorPacientes);

                if (personaLogueada == null) {
                    System.out.println("❌ Datos ingresados incorrectos o usuario no encontrado.");
                    if (intentos < maxIntentos) {
                        System.out.println("Intentá nuevamente.\n");
                    }
                }

            } catch (CredencialesIncorrectasException e) {
                System.out.println("❌ " + e.getMessage());
                if (intentos < maxIntentos) {
                    System.out.println("Intentá nuevamente.\n");
                }
            } catch (Exception e) {
                System.err.println("⚠️ Ocurrió un error inesperado al intentar iniciar sesión: " + e.getMessage());
                if (intentos < maxIntentos) {
                    System.out.println("Podés intentar nuevamente.\n");
                }
            }
        }

        if (personaLogueada == null) {
            System.out.println("🚪 Se alcanzó el máximo de intentos permitidos. Cerrando programa...");
            sc.close();
            return;
        }

        System.out.println("\n✅ Bienvenido, " + personaLogueada.getNombre() + " (" + personaLogueada.getTipo() + ")");

        boolean salir = false;
        while (!salir) {
            try {
                // MENU PACIENTE
                if (personaLogueada instanceof Paciente paciente) {
                    System.out.println("\n--- MENU PACIENTE ---");
                    System.out.println("1. Sacar turno");
                    System.out.println("2. Ver historia clinica");
                    System.out.println("3. Ver recetas emitidas");
                    System.out.println("0. Volver / Salir");
                    System.out.print("Opcion: ");
                    int op = leerOpcion(sc);

                    switch (op) {
                        case 1 ->
                            paciente.sacarTurno(gestorEmpleados.getListaProfesionales(), gestorPacientes, gestorEmpleados);


                        case 2 -> System.out.println(paciente.getHistoriaClinica());
                        case 3 -> paciente.mostrarRecetas();
                        case 0 -> salir = true;
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
                    System.out.println("6. Consultar recetas de paciente");
                    System.out.println("0. Volver / Salir");
                    System.out.print("Opcion: ");
                    int op = leerOpcion(sc);

                    switch (op) {
                        case 1 -> profesional.consultarAgenda();
                        case 2 -> profesional.mostrarDisponibilidad();
                        case 3 -> profesional.mostrarTurnosOcupados();
                        case 4 -> {
                            System.out.print("DNI del paciente: ");
                            String dni = sc.nextLine();
                            try {
                                Paciente pac = gestorPacientes.buscarPacientePorDni(dni);
                                if (pac == null) {
                                    throw new excepciones.PacienteNoEncontradoException(
                                        "No se encontró un paciente con DNI: " + dni
                                    );
                                }
                                System.out.print("Diagnostico: ");
                                String diag = sc.nextLine();
                                System.out.print("Medicamento: ");
                                String med = sc.nextLine();
                                System.out.print("Dosis: ");
                                String dosis = sc.nextLine();
                                Receta receta = new Receta(UUID.randomUUID().toString(), diag, med, dosis);
                                profesional.agregarRecetaAHistoria(pac.getHistoriaClinica(), receta, gestorPacientes);
                                System.out.println("✅ Receta agregada y guardada correctamente.");
                            } catch (excepciones.PacienteNoEncontradoException e) {
                                System.out.println("❌ " + e.getMessage());
                            } catch (Exception e) {
                                System.out.println("❌ Error al agregar receta: " + e.getMessage());
                            }
                        }
                        case 5 -> {
                            System.out.print("DNI del paciente: ");
                            String dni = sc.nextLine();
                            try {
                                Paciente pac = gestorPacientes.buscarPacientePorDni(dni);
                                if (pac == null) {
                                    throw new excepciones.PacienteNoEncontradoException(
                                        "No se encontró un paciente con DNI: " + dni
                                    );
                                }
                                System.out.print("Descripcion: ");
                                String desc = sc.nextLine();
                                System.out.print("Tipo de antecedente: ");
                                String tipo = sc.nextLine();
                                Antecedentes ant = new Antecedentes(UUID.randomUUID().toString(), desc, tipo);
                                profesional.agregarAntecedenteAHistoria(pac.getHistoriaClinica(), ant, gestorPacientes);
                                System.out.println("✅ Antecedente agregado y guardado correctamente.");
                            } catch (excepciones.PacienteNoEncontradoException e) {
                                System.out.println("❌ " + e.getMessage());
                            } catch (Exception e) {
                                System.out.println("❌ Error al agregar antecedente: " + e.getMessage());
                            }
                        }
                        case 6 -> {
                            System.out.print("DNI del paciente para consultar recetas: ");
                            String dni = sc.nextLine();
                            try {
                                Paciente pac = gestorPacientes.buscarPacientePorDni(dni);
                                if (pac == null) {
                                    throw new excepciones.PacienteNoEncontradoException(
                                        "No se encontró un paciente con DNI: " + dni
                                    );
                                }
                                // Consultar recetas directamente desde el paciente
                                List<Receta> recetas = pac.consultarRecetas(dni);
                                System.out.println("\n=== RECETAS DEL PACIENTE ===");
                                System.out.println("Paciente: " + pac.getNombre() + " " + pac.getApellido() + " (DNI: " + dni + ")");
                                System.out.println("Total de recetas: " + recetas.size());
                                System.out.println("------------------------");
                                for (int i = 0; i < recetas.size(); i++) {
                                    System.out.println((i + 1) + ". " + recetas.get(i));
                                }
                            } catch (excepciones.PacienteNoEncontradoException e) {
                                System.out.println("❌ " + e.getMessage());
                            } catch (excepciones.RecetasNoDisponiblesException e) {
                                System.out.println("⚠️ " + e.getMessage());
                            } catch (excepciones.HistoriaClinicaNoEncontradaException e) {
                                System.out.println("❌ " + e.getMessage());
                            } catch (Exception e) {
                                System.out.println("❌ Error al consultar recetas: " + e.getMessage());
                            }
                        }
                        case 0 -> salir = true;
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
                    System.out.println("0. Volver / Salir");
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
                            System.out.println("0. Volver atrás");
                            System.out.print("Opcion: ");
                            int tipo = leerOpcion(sc);

                            switch (tipo) {
                                case 1 -> altaProfesional(gestorEmpleados, sc);
                                case 2 -> altaAdministrativo(gestorEmpleados, sc);
                                case 3 -> altaPaciente(gestorPacientes, sc);
                                case 0 -> System.out.println("Volviendo al menú principal...");
                                default -> System.out.println("Opcion invalida.");
                            }
                        }
                        case 4 -> {
                            System.out.println("\n¿Que tipo de ususario desea modificar/eliminar?");
                            System.out.println("1. Empleado");
                            System.out.println("2. Paciente");
                            System.out.println("0. Volver atrás");
                            System.out.print("Opcion: ");
                            int tipo = leerOpcion(sc);
                            switch (tipo) {
                                case 1 -> {
                                    System.out.println("\n1. Modificar empleado");
                                    System.out.println("2. Eliminar empleado");
                                    System.out.println("0. Volver atrás");
                                    System.out.print("Opcion: ");
                                    int opEmp = leerOpcion(sc);
                                    if (opEmp == 1) modificarEmpleado(gestorEmpleados, sc);
                                    else if (opEmp == 2) eliminarEmpleado(gestorEmpleados, sc);
                                    else if (opEmp == 0) System.out.println("Volviendo al menú principal...");
                                    else System.out.println("Opción inválida.");
                                }
                                case 2 -> {
                                    System.out.println("\n1. Modificar paciente");
                                    System.out.println("2. Eliminar paciente");
                                    System.out.println("0. Volver atrás");
                                    System.out.print("Opcion: ");
                                    int opPac = leerOpcion(sc);
                                    if (opPac == 1) modificarPaciente(gestorPacientes, sc);
                                    else if (opPac == 2) eliminarPaciente(gestorPacientes, sc);
                                    else if (opPac == 0) System.out.println("Volviendo al menú principal...");
                                    else System.out.println("Opción inválida.");
                                }
                                case 0 -> System.out.println("Volviendo al menú principal...");
                                default -> System.out.println("Opción inválida.");
                            }
                        }

                        case 0 -> salir = true;
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

    private static void altaProfesional(GestorEmpleadosJson gestor, Scanner sc) {
        System.out.println("\n--- Alta de Profesional ---");
        String dni = InputUtils.readDni(sc);
        String nombre = InputUtils.readNonEmpty(sc, "Nombre");
        String apellido = InputUtils.readNonEmpty(sc, "Apellido");
        String nac = InputUtils.readNonEmpty(sc, "Nacionalidad");
        String calle = InputUtils.readNonEmpty(sc, "Calle");
        int numero = InputUtils.readInt(sc, "Número");
        String depto = InputUtils.readNonEmpty(sc, "Depto");
        String ciudad = InputUtils.readNonEmpty(sc, "Ciudad");
        String provincia = InputUtils.readNonEmpty(sc, "Provincia");
        String correo = InputUtils.readEmail(sc);
        String pass = InputUtils.readNonEmpty(sc, "Contrasenia");
        LocalDate fechaNac = InputUtils.readDate(sc, "Fecha de nacimiento");
        String legajo = InputUtils.readAlfanumerico(sc, "Legajo", 3, 12);
        String matricula = InputUtils.readAlfanumerico(sc, "Matricula", 3, 20);
        Especialidad especialidad = InputUtils.readEnum(sc, "Especialidad", Especialidad.class); // :contentReference[oaicite:7]{index=7}

        // Si tu gestor fabrica Direccion internamente a partir de strings:
        gestor.agregarEmpleado(
                "Profesional", dni, nombre, apellido, nac, calle, numero, depto, ciudad, provincia,
                correo, pass, fechaNac.toString(), legajo, matricula, especialidad.name(), null
        );
        gestor.cargarEmpleadoDesdeJson();
        System.out.println("✅ Profesional agregado y guardado en empleados.json");
    }

    private static void altaAdministrativo(GestorEmpleadosJson gestor, Scanner sc) {
        System.out.println("\n--- Alta de Administrativo ---");
        String dni = InputUtils.readDni(sc);
        String nombre = InputUtils.readNonEmpty(sc, "Nombre");
        String apellido = InputUtils.readNonEmpty(sc, "Apellido");
        String nac = InputUtils.readNonEmpty(sc, "Nacionalidad");
        String calle = InputUtils.readNonEmpty(sc, "Calle");
        int numero = InputUtils.readInt(sc, "Número");
        String depto = InputUtils.readNonEmpty(sc, "Depto");
        String ciudad = InputUtils.readNonEmpty(sc, "Ciudad");
        String provincia = InputUtils.readNonEmpty(sc, "Provincia");
        String correo = InputUtils.readEmail(sc);
        String pass = InputUtils.readNonEmpty(sc, "Contrasenia");
        LocalDate fechaNac = InputUtils.readDate(sc, "Fecha de nacimiento");
        String legajo = InputUtils.readAlfanumerico(sc, "Legajo", 3, 12);
        String sector = InputUtils.readNonEmpty(sc, "Sector");

        gestor.agregarEmpleado(
                "Administrativo", dni, nombre, apellido, nac, calle, numero, depto, ciudad, provincia,
                correo, pass, fechaNac.toString(), legajo, null, null, sector
        ); // Constructor de clase pide 'sector'. :contentReference[oaicite:8]{index=8}
        gestor.cargarEmpleadoDesdeJson();
        System.out.println("✅ Administrativo agregado y guardado en empleados.json");
    }

    private static void altaPaciente(GestorPacientesJson gestor, Scanner sc) {
        System.out.println("\n--- Alta de Paciente ---");
        String dni = InputUtils.readDni(sc);
        String nombre = InputUtils.readNonEmpty(sc, "Nombre");
        String apellido = InputUtils.readNonEmpty(sc, "Apellido");
        String nac = InputUtils.readNonEmpty(sc, "Nacionalidad");
        String calle = InputUtils.readNonEmpty(sc, "Calle");
        int numero = InputUtils.readInt(sc, "Número");
        String depto = InputUtils.readNonEmpty(sc, "Depto");
        String ciudad = InputUtils.readNonEmpty(sc, "Ciudad");
        String provincia = InputUtils.readNonEmpty(sc, "Provincia");
        String correo = InputUtils.readEmail(sc);
        String pass = InputUtils.readNonEmpty(sc, "Contrasenia");
        LocalDate fechaNac = InputUtils.readDate(sc, "Fecha de nacimiento");
        String afiliado = InputUtils.readAlfanumerico(sc, "N Afiliado", 3, 20);
        ObraSocial obra = InputUtils.readEnum(sc, "Obra Social", ObraSocial.class); // Valida enum

        gestor.agregarPaciente(
                dni, nombre, apellido, nac, calle, numero, depto, ciudad, provincia,
                correo, pass, fechaNac.toString(), afiliado, obra.name()
        ); // El ctor de Paciente maneja HC para evitar NPE. :contentReference[oaicite:9]{index=9}
        gestor.cargarPacienteDesdeJson();
        System.out.println("✅ Paciente agregado y guardado en pacientes.json");
    }

    //METODOS PARA MODIFICAR Y ELIMINAR EMPLEADOS Y PACIENTES
    private static void modificarPaciente(GestorPacientesJson gestor, Scanner sc) {
        System.out.print("Ingrese el DNI del paciente a modificar: ");
        String dni = sc.nextLine();
        
        try {
            Paciente pacienteObj = gestor.buscarPacientePorDni(dni);
            if (pacienteObj == null) {
                throw new excepciones.PacienteNoEncontradoException(
                    "No se encontró un paciente con DNI: " + dni
                );
            }
            
            JSONObject paciente = gestor.obtenerPaciente(dni);
            if (paciente == null) {
                throw new excepciones.PacienteNoEncontradoException(
                    "No se encontró un paciente con DNI: " + dni
                );
            }

            System.out.println("\n=== MODIFICAR PACIENTE ===");
            System.out.println("Paciente actual: " + pacienteObj.getNombre() + " " + pacienteObj.getApellido());
            System.out.println("(Presione Enter para mantener el valor actual)\n");

            System.out.print("Nuevo nombre [" + pacienteObj.getNombre() + "]: ");
            String nombre = sc.nextLine();
            if (nombre.trim().isEmpty()) nombre = pacienteObj.getNombre();

            System.out.print("Nuevo apellido [" + pacienteObj.getApellido() + "]: ");
            String apellido = sc.nextLine();
            if (apellido.trim().isEmpty()) apellido = pacienteObj.getApellido();

            System.out.print("Nueva nacionalidad [" + pacienteObj.getNacionalidad() + "]: ");
            String nac = sc.nextLine();
            if (nac.trim().isEmpty()) nac = pacienteObj.getNacionalidad();

            System.out.print("Nueva calle [" + pacienteObj.getDireccion().getCalle() + "]: ");
            String calle = sc.nextLine();
            if (calle.trim().isEmpty()) calle = pacienteObj.getDireccion().getCalle();

            System.out.print("Nuevo número [" + pacienteObj.getDireccion().getNumero() + "]: ");
            String numStr = sc.nextLine();
            int numero;
            if (numStr.trim().isEmpty()) {
                numero = pacienteObj.getDireccion().getNumero();
            } else {
                try {
                    numero = Integer.parseInt(numStr);
                } catch (NumberFormatException e) {
                    throw new DatoInvalidoException("El número de calle debe ser un valor numérico.");
                }
            }

            System.out.print("Nuevo depto [" + pacienteObj.getDireccion().getDepartamento() + "]: ");
            String depto = sc.nextLine();
            if (depto.trim().isEmpty()) depto = pacienteObj.getDireccion().getDepartamento();

            System.out.print("Nueva ciudad [" + pacienteObj.getDireccion().getCiudad() + "]: ");
            String ciudad = sc.nextLine();
            if (ciudad.trim().isEmpty()) ciudad = pacienteObj.getDireccion().getCiudad();

            System.out.print("Nueva provincia [" + pacienteObj.getDireccion().getProvincia() + "]: ");
            String provincia = sc.nextLine();
            if (provincia.trim().isEmpty()) provincia = pacienteObj.getDireccion().getProvincia();

            System.out.print("Nuevo correo [" + pacienteObj.getCorreoElectronico() + "]: ");
            String correo = sc.nextLine();
            if (correo.trim().isEmpty()) correo = pacienteObj.getCorreoElectronico();

            System.out.print("Nueva contraseña (dejar vacío para mantener la actual): ");
            String pass = sc.nextLine();
            if (pass.trim().isEmpty()) {
                // Mantener la contraseña actual (obtenerla del JSON)
                pass = paciente.getString("contrasenia");
            }

            System.out.print("Nueva fecha de nacimiento (YYYY-MM-DD) [" + pacienteObj.getFechaNacimiento() + "]: ");
            String fecha = sc.nextLine();
            if (fecha.trim().isEmpty()) {
                fecha = pacienteObj.getFechaNacimiento().toString();
            } else {
                // Validar formato de fecha
                try {
                    LocalDate.parse(fecha);
                } catch (Exception e) {
                    throw new DatoInvalidoException("Formato de fecha inválido. Use YYYY-MM-DD.");
                }
            }

            System.out.print("Nuevo número de afiliado [" + pacienteObj.getNroAfiliado() + "]: ");
            String afiliado = sc.nextLine();
            if (afiliado.trim().isEmpty()) afiliado = pacienteObj.getNroAfiliado();

            System.out.print("Nueva obra social [" + pacienteObj.getObraSocial() + "]: ");
            String obra = sc.nextLine().toUpperCase();
            if (obra.trim().isEmpty()) {
                obra = pacienteObj.getObraSocial().name();
            } else {
                // Validar enum
                try {
                    ObraSocial.valueOf(obra);
                } catch (IllegalArgumentException e) {
                    throw new DatoInvalidoException("La obra social ingresada no es válida.");
                }
            }

            gestor.modificarPaciente(dni, nombre, apellido, nac, calle, numero, depto, ciudad,
                    provincia, correo, pass, fecha, afiliado, obra);
            gestor.cargarPacienteDesdeJson();
            System.out.println("\n✅ Paciente modificado correctamente.");
        } catch (excepciones.PacienteNoEncontradoException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (DatoInvalidoException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error al modificar paciente: " + e.getMessage());
        }
    }

    private static void eliminarPaciente(GestorPacientesJson gestor, Scanner sc) {
        System.out.print("Ingrese el DNI del paciente a eliminar: ");
        String dni = sc.nextLine();
        
        try {
            Paciente pacienteObj = gestor.buscarPacientePorDni(dni);
            if (pacienteObj == null) {
                throw new excepciones.PacienteNoEncontradoException(
                    "No se encontró un paciente con DNI: " + dni
                );
            }
            
            System.out.println("\n⚠️ ADVERTENCIA: Está por eliminar al paciente:");
            System.out.println("   Nombre: " + pacienteObj.getNombre() + " " + pacienteObj.getApellido());
            System.out.println("   DNI: " + pacienteObj.getDni());
            System.out.print("¿Está seguro? (escriba 'SI' para confirmar): ");
            String confirmacion = sc.nextLine();
            
            if (!confirmacion.equalsIgnoreCase("SI")) {
                System.out.println("❌ Operación cancelada.");
                return;
            }
            
            gestor.eliminarPaciente(dni);
            gestor.cargarPacienteDesdeJson();
            System.out.println("✅ Paciente eliminado correctamente.");
        } catch (excepciones.PacienteNoEncontradoException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar paciente: " + e.getMessage());
        }
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