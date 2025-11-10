package ManejoJSON;
import clases.*;
import enums.Especialidad;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GestorEmpleadosJson {

    private String archivoJson;
    private JSONArray empleados;
    private List<Persona> usuarios;
    private List<Empleado> listaEmpleados;
    private List<Profesional> listaProfesionales;

    public GestorEmpleadosJson(String archivoJson) {
        this.archivoJson = archivoJson;
        this.usuarios = new ArrayList<>();
        this.listaEmpleados = new ArrayList<>();
        this.listaProfesionales = new ArrayList<>();
        try {
            if (Files.exists(Paths.get(archivoJson))) {
                String content = new String(Files.readAllBytes(Paths.get(archivoJson)));
                this.empleados  = new JSONArray(content);
            } else {
                this.empleados = new JSONArray();
                guardar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CRUD JSON EMPLEADOS


    // Agregar un nuevo empleado
    public void agregarEmpleado(String tipo, String dni, String nombre, String apellido, String nacionalidad,
                                String calle, int numero, String depto, String ciudad, String provincia,
                                String correo, String contrasenia, String fechaNacimiento,
                                String legajo, String matricula, String especialidad, String sector) {
        try {
            JSONObject empleado = new JSONObject();
            empleado.put("tipo", tipo);
            empleado.put("dni", dni);
            empleado.put("nombre", nombre);
            empleado.put("apellido", apellido);
            empleado.put("nacionalidad", nacionalidad);

            JSONObject direccion = new JSONObject();
            direccion.put("calle", calle);
            direccion.put("numero", numero);
            direccion.put("departamento", depto);
            direccion.put("ciudad", ciudad);
            direccion.put("provincia", provincia);

            empleado.put("direccion", direccion);
            empleado.put("correoElectronico", correo);
            empleado.put("contrasenia", contrasenia);
            empleado.put("fechaNacimiento", fechaNacimiento);
            empleado.put("legajo", legajo);

            // Para profesional
            if (tipo.equalsIgnoreCase("Profesional")) {
                empleado.put("matricula", matricula);
                empleado.put("especialidad", especialidad);
                empleado.put("agenda", new JSONArray());
            }
            // Para administrativo
            else if (tipo.equalsIgnoreCase("Administrativo")) {
                empleado.put("sector", sector);
            }

            empleados.put(empleado);
            guardar();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Modificar un empleado existente por id
    public void modificarEmpleado(String dni, String nuevoNombre, String nuevoApellido, String nuevaNacionalidad,
                                  String calle, int numero, String depto, String ciudad, String provincia,
                                  String correo, String contrasenia, String fechaNacimiento,
                                  String legajo, String matricula, String especialidad, String sector)
    {
        try {
            for (int i = 0; i < empleados.length(); i++)
            {
                JSONObject empleado = empleados.getJSONObject(i);
                if(empleado.getString("dni").equals(dni))
                {
                    empleado.put("nombre", nuevoNombre);
                    empleado.put("apellido", nuevoApellido);
                    empleado.put("nacionalidad", nuevaNacionalidad);
                    empleado.put("correoElectronico", correo);
                    empleado.put("contrasenia", contrasenia);
                    empleado.put("fechaNacimiento", fechaNacimiento);
                    empleado.put("legajo", legajo);

                    JSONObject direccion = empleado.getJSONObject("direccion");
                    direccion.put("calle", calle);
                    direccion.put("numero", numero);
                    direccion.put("departamento", depto);
                    direccion.put("ciudad", ciudad);
                    direccion.put("provincia", provincia);

                    if (empleado.getString("tipo").equalsIgnoreCase("Profesional")) {
                        empleado.put("matricula", matricula);
                        empleado.put("especialidad", especialidad);
                    } else {
                        empleado.put("sector", sector);
                    }
                    break;
                }
            }
            guardar();
        } catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    // Eliminar un empleado por DNI
    public void eliminarEmpleado(String dni) {

        try {
            for (int i = 0; i < empleados.length(); i++)
            {
                JSONObject empleado = empleados.getJSONObject(i);
                if (empleado.getString("dni").equals(dni)) {
                    empleados.remove(i);
                    break;
                }
            }
            guardar();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Empleado> getListaEmpleados() { return listaEmpleados; }
    public List<Profesional> getListaProfesionales() { return listaProfesionales; }

    // Listar todos los empleados
    public void listarEmpleados() {
        try {
            for (int i = 0; i < empleados.length(); i++) {
                System.out.println(empleados.getJSONObject(i).toString(4));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Obtener un empleado por DNI
    public JSONObject obtenerEmpleado(String dni) {
        try {
            for (int i = 0; i < empleados.length(); i++) {
                JSONObject empleado = empleados.getJSONObject(i);
                if (empleado.getString("dni").equals(dni)) {
                    return empleado;
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //ACTUALIZAR PROFESIONAL
    public void actualizarProfesional(Profesional profesional) {
        for (int i = 0; i < listaProfesionales.size(); i++) {
            if (listaProfesionales.get(i).getDni().equals(profesional.getDni())) {
                listaProfesionales.set(i, profesional);
                return;
            }
        }
    }

    // Guardar cambios en el archivo JSON
    private void guardar() {
        try {
            Files.write(Paths.get(archivoJson), empleados.toString(4).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void guardarEmpleados() {
        guardar();
    }

    //SINCRONIZAR CON JSON
    public void sincronizarProfesionalConJson(Profesional profesional) {
        try {
            for (int i = 0; i < empleados.length(); i++) {
                JSONObject empleado = empleados.getJSONObject(i);
                if (empleado.getString("dni").equals(profesional.getDni())) {

                    // Actualizamos la agenda
                    JSONArray nuevaAgenda = new JSONArray();
                    for (Turno t : profesional.getAgenda()) {
                        if (t.getIdPaciente() != null) {
                            JSONObject turnoJson = new JSONObject();
                            turnoJson.put("idTurno", t.getIdTurno());
                            turnoJson.put("idPaciente", t.getIdPaciente());
                            turnoJson.put("idProfesional", t.getIdProfesional());
                            turnoJson.put("dia", t.getDia().toString());
                            turnoJson.put("hora", t.getHora().toString());
                            nuevaAgenda.put(turnoJson);
                        }
                    }

                    empleado.put("agenda", nuevaAgenda);
                    break;
                }
            }
            guardar();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //CARGAR EMPLEADOS DESDE ARCHIVO JSON EN OBJETOS
    public void cargarEmpleadoDesdeJson()
    {
        try {
            listaEmpleados = new ArrayList<>();
            listaProfesionales = new ArrayList<>();
            usuarios = new ArrayList<>();

            for (int i = 0; i < empleados.length(); i++) {
                JSONObject obj = empleados.getJSONObject(i);

                // --- Dirección ---
                JSONObject dir = obj.getJSONObject("direccion");
                Direccion direccion = new Direccion(
                        dir.getString("calle"),
                        dir.getInt("numero"),
                        dir.getString("departamento"),
                        dir.getString("ciudad"),
                        dir.getString("provincia")
                );
                String tipo = obj.getString("tipo");

                // --- Si es un profesional ---
                if (tipo.equalsIgnoreCase("Profesional")) {
                    List<Turno> turnosOcupados  = new ArrayList<>();
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
                            if (turno.getIdPaciente() != null) { // Solo turnos realmente ocupados
                                turnosOcupados.add(turno);
                            }
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
                            Especialidad.valueOf(obj.getString("especialidad").toUpperCase())
                    );
                    // Generar agenda completa de turnos libres
                    profesional.generarAgenda();

                    // Insertar los turnos ocupados dentro de esa agenda
                    for (Turno ocupado : turnosOcupados) {
                        for (Turno libre : profesional.getAgenda()) {
                            if (libre.getDia().equals(ocupado.getDia()) && libre.getHora().equals(ocupado.getHora())) {
                                libre.setIdPaciente(ocupado.getIdPaciente());
                                break;
                            }
                        }
                    }
                    // Agregar el profesional a las listas
                    listaProfesionales.add(profesional);
                    listaEmpleados.add(profesional);
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

                    listaEmpleados.add(administrativo);
                }
            }
            if (usuarios == null) {
                usuarios = new ArrayList<>();
            }
            usuarios.clear();
            usuarios.addAll(listaEmpleados);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}