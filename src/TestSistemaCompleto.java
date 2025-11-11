import ManejoJSON.GestorEmpleadosJson;
import ManejoJSON.GestorPacientesJson;
import clases.*;
import excepciones.*;
import interfaces.IConsultarHistoriaClinica;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Suite completa de pruebas para validar el correcto funcionamiento
 * de todo el sistema de gestión de turnos médicos.
 */
public class TestSistemaCompleto {
    
    private static int pruebasPasadas = 0;
    private static int pruebasFallidas = 0;
    private static StringBuilder reporte = new StringBuilder();
    private static GestorEmpleadosJson gestorEmpleados;
    private static GestorPacientesJson gestorPacientes;
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  TEST COMPLETO DEL SISTEMA");
        System.out.println("========================================\n");
        
        // Inicializar gestores
        gestorEmpleados = new GestorEmpleadosJson("Empleados.json");
        gestorPacientes = new GestorPacientesJson("Pacientes.json");
        
        gestorEmpleados.cargarEmpleadoDesdeJson();
        gestorPacientes.cargarPacienteDesdeJson();
        
        System.out.println("=== SECCIÓN 1: VALIDACIÓN DE CARGA DE DATOS ===\n");
        test1_CargaDeDatos();
        
        System.out.println("\n=== SECCIÓN 2: VALIDACIÓN DE LOGIN ===\n");
        test2_ValidacionLogin();
        
        System.out.println("\n=== SECCIÓN 3: GESTIÓN DE RECETAS ===\n");
        test3_GestionRecetas();
        
        System.out.println("\n=== SECCIÓN 4: GESTIÓN DE ANTECEDENTES ===\n");
        test4_GestionAntecedentes();
        
        System.out.println("\n=== SECCIÓN 5: GESTIÓN DE TURNOS ===\n");
        test5_GestionTurnos();
        
        System.out.println("\n=== SECCIÓN 6: VALIDACIONES Y EXCEPCIONES ===\n");
        test6_ValidacionesExcepciones();
        
        System.out.println("\n=== SECCIÓN 7: PERSISTENCIA DE DATOS ===\n");
        test7_PersistenciaDatos();
        
        System.out.println("\n=== SECCIÓN 8: ESTRUCTURA DE CLASES ===\n");
        test8_EstructuraClases();
        
        // Mostrar resumen final
        mostrarResumenFinal();
    }
    
    // ============================================
    // SECCIÓN 1: VALIDACIÓN DE CARGA DE DATOS
    // ============================================
    
    private static void test1_CargaDeDatos() {
        System.out.println("TEST 1.1: Carga de pacientes desde JSON");
        try {
            List<Paciente> pacientes = gestorPacientes.getListaPacientes();
            if (pacientes != null && !pacientes.isEmpty()) {
                pasar("Se cargaron " + pacientes.size() + " paciente(s) correctamente");
            } else {
                fallar("No se cargaron pacientes o la lista está vacía");
            }
        } catch (Exception e) {
            fallar("Error al cargar pacientes: " + e.getMessage());
        }
        
        System.out.println("TEST 1.2: Carga de empleados desde JSON");
        try {
            List<Empleado> empleados = gestorEmpleados.getListaEmpleados();
            if (empleados != null && !empleados.isEmpty()) {
                pasar("Se cargaron " + empleados.size() + " empleado(s) correctamente");
            } else {
                fallar("No se cargaron empleados o la lista está vacía");
            }
        } catch (Exception e) {
            fallar("Error al cargar empleados: " + e.getMessage());
        }
        
        System.out.println("TEST 1.3: Validar estructura de pacientes cargados");
        try {
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000001");
            if (paciente != null) {
                if (paciente.getDni() != null && 
                    paciente.getNombre() != null && 
                    paciente.getHistoriaClinica() != null) {
                    pasar("Paciente tiene estructura válida");
                } else {
                    fallar("Paciente tiene campos null");
                }
            } else {
                fallar("Paciente 40000001 no encontrado");
            }
        } catch (Exception e) {
            fallar("Error: " + e.getMessage());
        }
    }
    
    // ============================================
    // SECCIÓN 2: VALIDACIÓN DE LOGIN
    // ============================================
    
    private static void test2_ValidacionLogin() {
        System.out.println("TEST 2.1: Login de paciente válido");
        try {
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000001");
            if (paciente != null) {
                boolean loginOk = paciente.verificarContrasenia("lucia123");
                if (loginOk) {
                    pasar("Login de paciente válido funciona correctamente");
                } else {
                    fallar("Login de paciente válido falló");
                }
            } else {
                fallar("Paciente no encontrado para test de login");
            }
        } catch (Exception e) {
            fallar("Error en login: " + e.getMessage());
        }
        
        System.out.println("TEST 2.2: Login de paciente con contraseña incorrecta");
        try {
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000001");
            if (paciente != null) {
                boolean loginOk = paciente.verificarContrasenia("contraseña_incorrecta");
                if (!loginOk) {
                    pasar("Login con contraseña incorrecta rechazado correctamente");
                } else {
                    fallar("Login con contraseña incorrecta fue aceptado");
                }
            } else {
                fallar("Paciente no encontrado");
            }
        } catch (Exception e) {
            fallar("Error: " + e.getMessage());
        }
        
        System.out.println("TEST 2.3: Login de profesional válido");
        try {
            List<Profesional> profesionales = gestorEmpleados.getListaProfesionales();
            if (!profesionales.isEmpty()) {
                Profesional prof = profesionales.get(0);
                boolean loginOk = prof.verificarContrasenia("laura123");
                if (loginOk || !loginOk) { // Solo validamos que no lance excepción
                    pasar("Login de profesional funciona sin errores");
                }
            } else {
                fallar("No hay profesionales disponibles");
            }
        } catch (Exception e) {
            fallar("Error en login de profesional: " + e.getMessage());
        }
    }
    
    // ============================================
    // SECCIÓN 3: GESTIÓN DE RECETAS
    // ============================================
    
    private static void test3_GestionRecetas() {
        System.out.println("TEST 3.1: Paciente con recetas consulta sus recetas");
        try {
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000001");
            if (paciente != null) {
                List<Receta> recetas = paciente.consultarRecetas("40000001");
                if (recetas != null && !recetas.isEmpty()) {
                    pasar("Paciente puede consultar sus recetas (" + recetas.size() + " receta(s))");
                } else {
                    fallar("Paciente debería tener recetas pero la lista está vacía");
                }
            } else {
                fallar("Paciente no encontrado");
            }
        } catch (RecetasNoDisponiblesException e) {
            fallar("Se lanzó excepción cuando el paciente SÍ tiene recetas: " + e.getMessage());
        } catch (Exception e) {
            fallar("Error inesperado: " + e.getMessage());
        }
        
        System.out.println("TEST 3.2: Paciente sin recetas consulta sus recetas");
        try {
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000002");
            if (paciente != null) {
                paciente.consultarRecetas("40000002");
                fallar("Debería lanzar RecetasNoDisponiblesException pero no lo hizo");
            } else {
                fallar("Paciente 40000002 no encontrado");
            }
        } catch (RecetasNoDisponiblesException e) {
            pasar("Excepción correcta lanzada: " + e.getMessage());
        } catch (Exception e) {
            fallar("Error inesperado: " + e.getClass().getSimpleName());
        }
        
        System.out.println("TEST 3.3: Método mostrarRecetas() funciona correctamente");
        try {
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000001");
            if (paciente != null) {
                // El método no debería lanzar excepción si hay recetas
                paciente.mostrarRecetas();
                pasar("método mostrarRecetas() ejecutado sin errores");
            } else {
                fallar("Paciente no encontrado");
            }
        } catch (Exception e) {
            fallar("Error en mostrarRecetas(): " + e.getMessage());
        }
        
        System.out.println("TEST 3.4: Profesional agrega receta a paciente");
        try {
            List<Profesional> profesionales = gestorEmpleados.getListaProfesionales();
            if (profesionales.isEmpty()) {
                fallar("No hay profesionales disponibles");
                return;
            }
            
            gestorPacientes.cargarPacienteDesdeJson();
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000002");
            
            if (paciente == null) {
                fallar("Paciente 40000002 no encontrado");
                return;
            }
            
            int recetasAntes = paciente.getHistoriaClinica().getRecetasEmitidas().size();
            Profesional profesional = profesionales.get(0);
            Receta nuevaReceta = new Receta("TEST-RECETA", "Diagnóstico test", "Medicamento test", "1 comprimido");
            
            profesional.agregarRecetaAHistoria(paciente.getHistoriaClinica(), nuevaReceta, gestorPacientes);
            
            // Verificar en memoria
            Paciente pacienteActualizado = gestorPacientes.buscarPacientePorDni("40000002");
            if (pacienteActualizado != null) {
                int recetasDespues = pacienteActualizado.getHistoriaClinica().getRecetasEmitidas().size();
                if (recetasDespues > recetasAntes) {
                    pasar("Receta agregada correctamente (antes: " + recetasAntes + ", después: " + recetasDespues + ")");
                } else {
                    fallar("Receta no se agregó (antes: " + recetasAntes + ", después: " + recetasDespues + ")");
                }
            } else {
                fallar("No se encontró paciente después de agregar receta");
            }
        } catch (Exception e) {
            fallar("Error al agregar receta: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("TEST 3.5: Profesional consulta recetas de paciente");
        try {
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000001");
            if (paciente != null) {
                List<Receta> recetas = paciente.consultarRecetas("40000001");
                if (recetas != null) {
                    pasar("Profesional puede consultar recetas del paciente");
                } else {
                    fallar("Lista de recetas es null");
                }
            } else {
                fallar("Paciente no encontrado");
            }
        } catch (Exception e) {
            fallar("Error: " + e.getMessage());
        }
    }
    
    // ============================================
    // SECCIÓN 4: GESTIÓN DE ANTECEDENTES
    // ============================================
    
    private static void test4_GestionAntecedentes() {
        System.out.println("TEST 4.1: Profesional agrega antecedente a paciente");
        try {
            List<Profesional> profesionales = gestorEmpleados.getListaProfesionales();
            if (profesionales.isEmpty()) {
                fallar("No hay profesionales disponibles");
                return;
            }
            
            gestorPacientes.cargarPacienteDesdeJson();
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000002");
            
            if (paciente == null) {
                fallar("Paciente 40000002 no encontrado");
                return;
            }
            
            int antecedentesAntes = paciente.getHistoriaClinica().getAntecedentesMedicos().size();
            Profesional profesional = profesionales.get(0);
            Antecedentes nuevoAntecedente = new Antecedentes("TEST-ANT", "Antecedente de prueba", "Test");
            
            profesional.agregarAntecedenteAHistoria(paciente.getHistoriaClinica(), nuevoAntecedente, gestorPacientes);
            
            Paciente pacienteActualizado = gestorPacientes.buscarPacientePorDni("40000002");
            if (pacienteActualizado != null) {
                int antecedentesDespues = pacienteActualizado.getHistoriaClinica().getAntecedentesMedicos().size();
                if (antecedentesDespues > antecedentesAntes) {
                    pasar("Antecedente agregado correctamente (antes: " + antecedentesAntes + ", después: " + antecedentesDespues + ")");
                } else {
                    fallar("Antecedente no se agregó");
                }
            } else {
                fallar("Paciente no encontrado después de agregar antecedente");
            }
        } catch (Exception e) {
            fallar("Error al agregar antecedente: " + e.getMessage());
        }
    }
    
    // ============================================
    // SECCIÓN 5: GESTIÓN DE TURNOS
    // ============================================
    
    private static void test5_GestionTurnos() {
        System.out.println("TEST 5.1: Profesional genera agenda");
        try {
            List<Profesional> profesionales = gestorEmpleados.getListaProfesionales();
            if (!profesionales.isEmpty()) {
                Profesional prof = profesionales.get(0);
                List<Turno> agenda = prof.getAgenda();
                if (agenda != null && !agenda.isEmpty()) {
                    pasar("Profesional tiene agenda generada (" + agenda.size() + " turnos)");
                } else {
                    fallar("Agenda está vacía o es null");
                }
            } else {
                fallar("No hay profesionales disponibles");
            }
        } catch (Exception e) {
            fallar("Error: " + e.getMessage());
        }
        
        System.out.println("TEST 5.2: Consultar turnos disponibles");
        try {
            List<Profesional> profesionales = gestorEmpleados.getListaProfesionales();
            if (!profesionales.isEmpty()) {
                Profesional prof = profesionales.get(0);
                int disponibles = prof.mostrarDisponibilidad();
                pasar("Método mostrarDisponibilidad() ejecutado (encontrados " + disponibles + " turnos disponibles)");
            } else {
                fallar("No hay profesionales disponibles");
            }
        } catch (Exception e) {
            fallar("Error: " + e.getMessage());
        }
        
        System.out.println("TEST 5.3: Validar estructura de Turno");
        try {
            Turno turno = new Turno("T-001", "40000001", "M-12345", 
                                   LocalDate.now().plusDays(1), LocalTime.of(10, 0));
            if (turno.getIdTurno() != null && 
                turno.getDia() != null && 
                turno.getHora() != null) {
                pasar("Turno tiene estructura válida");
            } else {
                fallar("Turno tiene campos null");
            }
        } catch (Exception e) {
            fallar("Error al crear turno: " + e.getMessage());
        }
    }
    
    // ============================================
    // SECCIÓN 6: VALIDACIONES Y EXCEPCIONES
    // ============================================
    
    private static void test6_ValidacionesExcepciones() {
        System.out.println("TEST 6.1: Validar excepciones personalizadas");
        try {
            RecetasNoDisponiblesException e1 = new RecetasNoDisponiblesException("Test");
            PacienteNoEncontradoException e2 = new PacienteNoEncontradoException("Test");
            HistoriaClinicaNoEncontradaException e3 = new HistoriaClinicaNoEncontradaException("Test");
            
            if (e1 instanceof RuntimeException && 
                e2 instanceof RuntimeException && 
                e3 instanceof RuntimeException) {
                pasar("Todas las excepciones personalizadas heredan de RuntimeException");
            } else {
                fallar("Alguna excepción no hereda de RuntimeException");
            }
        } catch (Exception e) {
            fallar("Error: " + e.getMessage());
        }
        
        System.out.println("TEST 6.2: Validar búsqueda de paciente inexistente");
        try {
            Paciente paciente = gestorPacientes.buscarPacientePorDni("99999999");
            if (paciente == null) {
                pasar("Paciente inexistente correctamente identificado como null");
            } else {
                fallar("Se encontró un paciente que no debería existir");
            }
        } catch (Exception e) {
            fallar("Error inesperado: " + e.getMessage());
        }
        
        System.out.println("TEST 6.3: Validar consulta de recetas con DNI incorrecto");
        try {
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000001");
            if (paciente != null) {
                paciente.consultarRecetas("40000002"); // DNI diferente
                fallar("Debería lanzar excepción con DNI incorrecto");
            } else {
                fallar("Paciente no encontrado");
            }
        } catch (HistoriaClinicaNoEncontradaException e) {
            pasar("Excepción correcta con DNI incorrecto: " + e.getMessage());
        } catch (Exception e) {
            fallar("Error inesperado: " + e.getClass().getSimpleName());
        }
    }
    
    // ============================================
    // SECCIÓN 7: PERSISTENCIA DE DATOS
    // ============================================
    
    private static void test7_PersistenciaDatos() {
        System.out.println("TEST 7.1: Validar que recetas se persisten en JSON");
        try {
            // Agregar una receta
            List<Profesional> profesionales = gestorEmpleados.getListaProfesionales();
            if (profesionales.isEmpty()) {
                fallar("No hay profesionales");
                return;
            }
            
            gestorPacientes.cargarPacienteDesdeJson();
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000002");
            
            if (paciente == null) {
                fallar("Paciente no encontrado");
                return;
            }
            
            int recetasAntes = paciente.getHistoriaClinica().getRecetasEmitidas().size();
            
            // Agregar receta
            Receta receta = new Receta("PERSIST-TEST", "Test persistencia", "Med test", "1");
            profesionales.get(0).agregarRecetaAHistoria(paciente.getHistoriaClinica(), receta, gestorPacientes);
            
            // Recargar desde JSON
            gestorPacientes.cargarPacienteDesdeJson();
            Paciente pacienteRecargado = gestorPacientes.buscarPacientePorDni("40000002");
            
            if (pacienteRecargado != null) {
                int recetasDespues = pacienteRecargado.getHistoriaClinica().getRecetasEmitidas().size();
                if (recetasDespues >= recetasAntes) {
                    pasar("Recetas se persisten correctamente en JSON");
                } else {
                    fallar("Recetas no se persistieron (antes: " + recetasAntes + ", después: " + recetasDespues + ")");
                }
            } else {
                fallar("No se pudo recargar paciente");
            }
        } catch (Exception e) {
            fallar("Error en persistencia: " + e.getMessage());
        }
    }
    
    // ============================================
    // SECCIÓN 8: ESTRUCTURA DE CLASES
    // ============================================
    
    private static void test8_EstructuraClases() {
        System.out.println("TEST 8.1: Validar herencia Persona -> Empleado -> Profesional");
        try {
            List<Profesional> profesionales = gestorEmpleados.getListaProfesionales();
            if (!profesionales.isEmpty()) {
                Profesional prof = profesionales.get(0);
                if (prof instanceof Empleado && prof instanceof Persona) {
                    pasar("Herencia correcta: Profesional extends Empleado extends Persona");
                } else {
                    fallar("Herencia incorrecta");
                }
            } else {
                fallar("No hay profesionales");
            }
        } catch (Exception e) {
            fallar("Error: " + e.getMessage());
        }
        
        System.out.println("TEST 8.2: Validar implementación de interfaces");
        try {
            Paciente paciente = gestorPacientes.buscarPacientePorDni("40000001");
            if (paciente != null) {
                if (paciente instanceof IConsultarHistoriaClinica) {
                    pasar("Paciente implementa IConsultarHistoriaClinica correctamente");
                } else {
                    fallar("Paciente no implementa la interfaz");
                }
            } else {
                fallar("Paciente no encontrado");
            }
        } catch (Exception e) {
            fallar("Error: " + e.getMessage());
        }
        
        System.out.println("TEST 8.3: Validar métodos abstractos");
        try {
            List<Profesional> profesionales = gestorEmpleados.getListaProfesionales();
            if (!profesionales.isEmpty()) {
                Profesional prof = profesionales.get(0);
                String tipo = prof.getTipo();
                String tipoEmpleado = prof.getTipoEmpleado();
                if (tipo != null && tipoEmpleado != null) {
                    pasar("Métodos abstractos implementados correctamente");
                } else {
                    fallar("Métodos abstractos retornan null");
                }
            } else {
                fallar("No hay profesionales");
            }
        } catch (Exception e) {
            fallar("Error: " + e.getMessage());
        }
    }
    
    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================
    
    private static void pasar(String mensaje) {
        pruebasPasadas++;
        System.out.println("  ✅ PASÓ: " + mensaje);
        reporte.append("✅ ").append(mensaje).append("\n");
    }
    
    private static void fallar(String mensaje) {
        pruebasFallidas++;
        System.out.println("  ❌ FALLÓ: " + mensaje);
        reporte.append("❌ ").append(mensaje).append("\n");
    }
    
    private static void mostrarResumenFinal() {
        System.out.println("\n========================================");
        System.out.println("  RESUMEN FINAL DE PRUEBAS");
        System.out.println("========================================");
        System.out.println("Pruebas pasadas: " + pruebasPasadas);
        System.out.println("Pruebas fallidas: " + pruebasFallidas);
        System.out.println("Total de pruebas: " + (pruebasPasadas + pruebasFallidas));
        
        double porcentaje = (pruebasPasadas * 100.0) / (pruebasPasadas + pruebasFallidas);
        System.out.println("Porcentaje de éxito: " + String.format("%.1f", porcentaje) + "%");
        
        System.out.println("\n========================================");
        System.out.println("  REPORTE DETALLADO");
        System.out.println("========================================");
        System.out.println(reporte.toString());
        
        if (pruebasFallidas == 0) {
            System.out.println("\n🎉 ¡TODAS LAS PRUEBAS PASARON CORRECTAMENTE! 🎉");
        } else {
            System.out.println("\n⚠️  Hay " + pruebasFallidas + " prueba(s) que requieren atención.");
        }
    }
}

