package ManejoJSON;
import clases.*;
import enums.ObraSocial;
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

public class GestorPacientesJson {

    private String archivoJson;
    private JSONArray pacientes;
    private List<Persona> usuarios;
    private List<Paciente> listaPacientes;

    public GestorPacientesJson(String archivoJson) {
        this.archivoJson = archivoJson;
        this.usuarios = new ArrayList<>();
        this.listaPacientes = new ArrayList<>();

        try {
            if (Files.exists(Paths.get(archivoJson))) {
                String content = new String(Files.readAllBytes(Paths.get(archivoJson)));
                this.pacientes = new JSONArray(content);
            } else {
                this.pacientes = new JSONArray();
                guardar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Paciente> getListaPacientes() {
        return listaPacientes;
    }
    //CRUD JSON

    // Agregar paciente
    public void agregarPaciente(String dni, String nombre, String apellido, String nacionalidad,
                                String calle, int numero, String depto, String ciudad, String provincia,
                                String correo, String contrasenia, String fechaNacimiento,
                                String nroAfiliado, String obraSocial) {
        try {
            JSONObject paciente = new JSONObject();
            paciente.put("dni", dni);
            paciente.put("nombre", nombre);
            paciente.put("apellido", apellido);
            paciente.put("nacionalidad", nacionalidad);

            JSONObject direccion = new JSONObject();
            direccion.put("calle", calle);
            direccion.put("numero", numero);
            direccion.put("departamento", depto);
            direccion.put("ciudad", ciudad);
            direccion.put("provincia", provincia);

            paciente.put("direccion", direccion);

            paciente.put("correoElectronico", correo);
            paciente.put("contrasenia", contrasenia);
            paciente.put("fechaNacimiento", fechaNacimiento);
            paciente.put("nroAfiliado", nroAfiliado);
            paciente.put("obraSocial", obraSocial);

            JSONObject historiaClinica = new JSONObject();
            historiaClinica.put("idHistoriaClinica", "HC-" + dni);
            historiaClinica.put("idPaciente", dni);
            historiaClinica.put("historialTurnos", new JSONArray());
            historiaClinica.put("recetasEmitidas", new JSONArray());
            historiaClinica.put("antecedentesMedicos", new JSONArray());
            paciente.put("historiaClinica", historiaClinica);

            pacientes.put(paciente);
            guardar();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Modificar paciente por DNI
    public void modificarPaciente(String dni, String nuevoNombre, String nuevoApellido, String nuevaNacionalidad,
                                  String calle, int numero, String depto, String ciudad, String provincia,
                                  String correo, String contrasenia, String fechaNacimiento,
                                  String nroAfiliado, String obraSocial) {
        try {
            for (int i = 0; i < pacientes.length(); i++) {
                JSONObject paciente = pacientes.getJSONObject(i);
                if (paciente.getString("dni").equals(dni)) {
                    paciente.put("nombre", nuevoNombre);
                    paciente.put("apellido", nuevoApellido);
                    paciente.put("nacionalidad", nuevaNacionalidad);
                    paciente.put("correoElectronico", correo);
                    paciente.put("contrasenia", contrasenia);
                    paciente.put("fechaNacimiento", fechaNacimiento);
                    paciente.put("nroAfiliado", nroAfiliado);
                    paciente.put("obraSocial", obraSocial);

                    JSONObject direccion = paciente.getJSONObject("direccion");
                    direccion.put("calle", calle);
                    direccion.put("numero", numero);
                    direccion.put("departamento", depto);
                    direccion.put("ciudad", ciudad);
                    direccion.put("provincia", provincia);
                    break;
                }
            }
            guardar();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Eliminar paciente por DNI
    public void eliminarPaciente(String dni) {
        try {
            for (int i = 0; i < pacientes.length(); i++) {
                JSONObject paciente = pacientes.getJSONObject(i);
                if (paciente.getString("dni").equals(dni)) {
                    pacientes.remove(i);
                    break;
                }
            }
            guardar();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Listar pacientes
    public void listarPacientes() {
        try {
            for (int i = 0; i < pacientes.length(); i++) {
                System.out.println(pacientes.getJSONObject(i).toString(4));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Obtener paciente por DNI
    public JSONObject obtenerPaciente(String dni) {
        try {
            for (int i = 0; i < pacientes.length(); i++) {
                JSONObject paciente = pacientes.getJSONObject(i);
                if (paciente.getString("dni").equals(dni)) {
                    return paciente;
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //BUSCAR PACIENTE
    public Paciente buscarPacientePorDni(String dni) {
        for (Paciente p : listaPacientes) {
            if (p.getDni().equals(dni)) return p;
        }
        return null;
    }

    public void actualizarPaciente(Paciente paciente) {
        for (int i = 0; i < listaPacientes.size(); i++) {
            if (listaPacientes.get(i).getDni().equals(paciente.getDni())) {
                listaPacientes.set(i, paciente);
                return;
            }
        }
    }

    //ACTUALIZAR PACIENTE

    // Guardar cambios en el archivo JSON
    private void guardar() {
        try {
            Files.write(Paths.get(archivoJson),
                    pacientes.toString(4).getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void guardarPacientes() {
        guardar();
    }

    //SINCRONIZAR CON JSON
    public void sincronizarPacienteConJson(Paciente paciente) {
        try {
            for (int i = 0; i < pacientes.length(); i++) {
                JSONObject pacObj = pacientes.getJSONObject(i);
                if (pacObj.getString("dni").equals(paciente.getDni())) {

                    JSONObject hcObj = pacObj.getJSONObject("historiaClinica");
                    JSONArray nuevosTurnos = new JSONArray();

                    for (Turno t : paciente.getHistoriaClinica().getHistorialTurnos()) {
                        JSONObject turnoJson = new JSONObject();
                        turnoJson.put("idTurno", t.getIdTurno());
                        turnoJson.put("idPaciente", t.getIdPaciente());
                        turnoJson.put("idProfesional", t.getIdProfesional());
                        turnoJson.put("dia", t.getDia().toString());
                        turnoJson.put("hora", t.getHora().toString());
                        nuevosTurnos.put(turnoJson); // 👈 Esta es la línea correcta
                    }

                    hcObj.put("historialTurnos", nuevosTurnos);
                    pacObj.put("historiaClinica", hcObj);
                    break;
                }
            }

            guardar();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //BAJAR DE JSON A OBJETOS

    public void cargarPacienteDesdeJson()
    {
        try {
            listaPacientes = new ArrayList<>();

            for (int i = 0; i < pacientes.length(); i++)
            {
                JSONObject obj = pacientes.getJSONObject(i);

                JSONObject dir = obj.getJSONObject("direccion");
                Direccion direccion = new Direccion(
                        dir.getString("calle"),
                        dir.getInt("numero"),
                        dir.getString("departamento"),
                        dir.getString("ciudad"),
                        dir.getString("provincia")

                );

                JSONObject hcObj = obj.getJSONObject("historiaClinica");

                // --- Historial de turnos ---
                List<Turno> historialTurnos = new ArrayList<>();
                JSONArray turnosArray = hcObj.getJSONArray("historialTurnos");
                for (int k = 0; k < turnosArray.length(); k++) {
                    // CLAVE: Obtener el objeto JSON individual
                    JSONObject turnoObj = turnosArray.getJSONObject(k);
                    Turno turno = new Turno(
                            turnoObj.getString("idTurno"),
                            turnoObj.getString("idPaciente"),
                            turnoObj.getString("idProfesional"),
                            LocalDate.parse(turnoObj.getString("dia")),
                            LocalTime.parse(turnoObj.getString("hora"))
                    );
                    historialTurnos.add(turno);
                }

                // --- Recetas emitidas ---
                List<Receta> recetasEmitidas = new ArrayList<>();
                JSONArray recetasArray = hcObj.getJSONArray("recetasEmitidas");
                for (int k = 0; k < recetasArray.length(); k++) {
                    // CLAVE: Obtener el objeto JSON individual
                    JSONObject recetaObj = recetasArray.getJSONObject(k);
                    Receta receta = new Receta(
                            recetaObj.getString("idReceta"),
                            recetaObj.getString("diagnostico"),
                            recetaObj.getString("medicamento"),
                            recetaObj.getString("dosis")
                    );
                    if (recetaObj.has("fechaEmision")) {
                        receta.setFechaEmision(LocalDate.parse(recetaObj.getString("fechaEmision")));
                    }
                    recetasEmitidas.add(receta);
                }

                // --- Antecedentes médicos ---
                List<Antecedentes> antecedentesMedicos = new ArrayList<>();
                JSONArray arrAntecedentes = hcObj.getJSONArray("antecedentesMedicos");
                for (int j = 0; j < arrAntecedentes.length(); j++) {
                    // CLAVE: Obtener el objeto JSON individual
                    JSONObject antObj = arrAntecedentes.getJSONObject(j); // <--- ESTO DEBERÍA SOLUCIONAR EL ERROR
                    Antecedentes antecedente = new Antecedentes(
                            antObj.getString("idRegistro"),
                            antObj.getString("descripcion"),
                            antObj.getString("tipoAntecedente")
                    );
                    if (antObj.has("fechaRegistro")) {
                        antecedente.setFechaRegistro(LocalDate.parse(antObj.getString("fechaRegistro")));
                    }
                    antecedentesMedicos.add(antecedente);
                }

                // Crear la historia clínica con sus listas
                HistoriaClinica historiaClinica = new HistoriaClinica(
                        hcObj.getString("idHistoriaClinica"),
                        hcObj.getString("idPaciente")
                );

                historiaClinica.setHistorialTurnos(historialTurnos);
                historiaClinica.setRecetasEmitidas(recetasEmitidas);
                historiaClinica.setAntecedentesMedicos(antecedentesMedicos);

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
                        ObraSocial.valueOf(obj.getString("obraSocial").trim().toUpperCase())

                );

                paciente.setHistoriaClinica(historiaClinica);
                listaPacientes.add(paciente);
            }
            if (usuarios == null) {
                usuarios = new ArrayList<>();
            }
            usuarios.addAll(listaPacientes);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}