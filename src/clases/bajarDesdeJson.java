package clases;

import ManejoJSON.JSONUtiles;
import clases.*;
import enums.Especialidad;
import enums.ObraSocial;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class bajarDesdeJson {

    private List<Persona> usuarios;
    private List<Paciente> pacientes;
    private List<Empleado> empleados;
    private List<Profesional> profesionales;

    public bajarDesdeJson(List<Persona> usuarios, List<Paciente> pacientes, List<Empleado> empleados, List<Profesional> profesionales) {
        this.usuarios = usuarios;
        this.pacientes = pacientes;
        this.empleados = empleados;
        this.profesionales = profesionales;
    }

    //CARGAR PACIENTES DESDE ARCHIVO JSON

    public void cargarPacienteDesdeJson(String rutaArchivo)
    {
        try {
            JSONArray array = new JSONArray(JSONUtiles.leer(rutaArchivo));
            pacientes = new ArrayList<>();

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject obj = array.getJSONObject(i);

                JSONObject dir = obj.getJSONObject("direccion");
                Direccion direccion = new Direccion(
                        dir.getString("calle"),
                        dir.getInt("numero"),
                        dir.getString("departamento"),
                        dir.getString("ciudad")
                );

                JSONObject hcObj = obj.getJSONObject("historiaClinica");

                // --- Historial de turnos ---
                List<Turno> historialTurnos = new ArrayList<>();
                JSONArray arrTurnos = hcObj.getJSONArray("historialTurnos");
                for (int j = 0; j < arrTurnos.length(); j++) {
                    JSONObject tObj = arrTurnos.getJSONObject(j);
                    Turno turno = new Turno(
                            tObj.getString("idTurno"),
                            tObj.optString("idPaciente", null),
                            tObj.getString("idProfesional"),
                            LocalDate.parse(tObj.getString("dia")),
                            LocalTime.parse(tObj.getString("hora"))
                    );
                    historialTurnos.add(turno);
                }

                // --- Recetas emitidas ---
                List<Receta> recetasEmitidas = new ArrayList<>();
                JSONArray recetasArray = hcObj.getJSONArray("recetasEmitidas");
                for (int k = 0; k < recetasArray.length(); k++) {
                    JSONObject recetaObj = recetasArray.getJSONObject(k);
                    Receta receta = new Receta(
                            recetaObj.getString("idReceta"),
                            recetaObj.getString("diagnostico"),
                            recetaObj.getString("medicamento"),
                            recetaObj.getString("dosis")
                    );
                    recetasEmitidas.add(receta);
                }

                // --- Antecedentes médicos ---
                List<String> antecedentes = new ArrayList<>();
                JSONArray arrAntecedentes = hcObj.getJSONArray("antecedentesMedicos");
                for (int j = 0; j < arrAntecedentes.length(); j++) {
                    antecedentes.add(arrAntecedentes.getString(j));
                }

                // Crear la historia clínica con sus listas
                HistoriaClinica historiaClinica = new HistoriaClinica(
                        hcObj.getString("idHistoriaClinica"),
                        hcObj.getString("idPaciente")
                );

                historiaClinica.setHistorialTurnos(historialTurnos);
                historiaClinica.setRecetasEmitidas(recetasEmitidas);
                historiaClinica.setAntecedentesMedicos(antecedentes);

                Paciente paciente = new Paciente(
                        obj.getString("dni"),
                        obj.getString("nombre"),
                        obj.getString("apellido"),
                        obj.getString("nacionalidad"),
                        direccion,
                        obj.getString("correoElectronico"),
                        obj.getString("contrasenia"),
                        LocalDate.parse(obj.getString("fechaNacimiento")),
                        obj.getString("nroAfiliado"),
                        ObraSocial.valueOf(obj.getString("obraSocial").toUpperCase())
                );

                paciente.setHistoriaClinica(historiaClinica);
                pacientes.add(paciente);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    //CARGAR PACIENTES DESDE ARCHIVO JSON

    public void cargarEmpleadoDesdeJson(String rutaArchivo)
    {
        try {
            JSONArray array = new JSONArray(JSONUtiles.leer(rutaArchivo));
            empleados = new ArrayList<>();
            profesionales = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String tipo = obj.optString("tipo", "");

                // --- Dirección ---
                JSONObject dir = obj.getJSONObject("direccion");
                Direccion direccion = new Direccion(
                        dir.getString("calle"),
                        dir.getInt("numero"),
                        dir.getString("departamento"),
                        dir.getString("ciudad")
                );

                // --- Si es un profesional ---
                if (tipo.equalsIgnoreCase("Profesional")) {
                    // Cargar agenda (lista de turnos)
                    List<Turno> agenda = new ArrayList<>();
                    JSONArray arrAgenda = obj.optJSONArray("agenda");
                    if (arrAgenda != null) {
                        for (int j = 0; j < arrAgenda.length(); j++) {
                            JSONObject tObj = arrAgenda.getJSONObject(j);
                            Turno turno = new Turno(
                                    tObj.getString("idTurno"),
                                    tObj.optString("idPaciente", null),
                                    tObj.getString("idProfesional"),
                                    LocalDate.parse(tObj.getString("dia")),
                                    LocalTime.parse(tObj.getString("hora"))
                            );
                            agenda.add(turno);
                        }
                    }

                    // Crear objeto Profesional
                    Profesional profesional = new Profesional(
                            obj.getString("dni"),
                            obj.getString("nombre"),
                            obj.getString("apellido"),
                            obj.getString("nacionalidad"),
                            direccion,
                            obj.getString("correoElectronico"),
                            obj.getString("contrasenia"),
                            LocalDate.parse(obj.getString("fechaNacimiento")),
                            obj.getString("legajo"),
                            obj.getString("matricula"),
                            Especialidad.valueOf(obj.getString("especialidad").toUpperCase()),
                            agenda
                    );

                    profesionales.add(profesional);
                    empleados.add(profesional);

                }
                // --- Si es un administrativo ---
                else if (tipo.equalsIgnoreCase("Administrativo")) {
                    Administrativo administrativo = new Administrativo(
                            obj.getString("dni"),
                            obj.getString("nombre"),
                            obj.getString("apellido"),
                            obj.getString("nacionalidad"),
                            direccion,
                            obj.getString("correoElectronico"),
                            obj.getString("contrasenia"),
                            LocalDate.parse(obj.getString("fechaNacimiento")),
                            obj.getString("legajo"),
                            obj.getString("sector")
                    );

                    empleados.add(administrativo);
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //MOSTRAR PACIENTES
    public void mostrarPacientes() {
        System.out.println("\n=== LISTA DE PACIENTES ===");
        for (Paciente p : pacientes) {
            System.out.println(p);
        }
    }
    //MOSTRAR EMPLEADOS
    public void mostrarEmpleados() {
        System.out.println("\n=== LISTA DE EMPLEADOS ===");
        for (Empleado e : empleados) {
            System.out.println(e);
        }
    }
}
