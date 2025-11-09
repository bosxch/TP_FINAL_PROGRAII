package ManejoJSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ModificarJsonPacientes {

    private String archivoJson;
    private JSONObject datos;

    public ModificarJsonPacientes(String archivoJson) {
        this.archivoJson = archivoJson;
        try {
            if (Files.exists(Paths.get(archivoJson))) {
                String content = new String(Files.readAllBytes(Paths.get(archivoJson)));
                datos = new JSONObject(content);
            } else {
                datos = new JSONObject();
                datos.put("pacientes", new JSONArray());
                guardar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONArray getPacientes() {
        try {
            return datos.getJSONArray("pacientes");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Agregar paciente
    public void agregarPaciente(String dni, String nombre, String apellido, String nacionalidad,
                                String calle, int numero, String depto, String ciudad, String provincia) {
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

            getPacientes().put(paciente);
            guardar();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Modificar paciente por DNI
    public void modificarPaciente(String dni, String nombre, String apellido, String nacionalidad,
                                  String calle, int numero, String depto, String ciudad, String provincia) {
        JSONArray pacientes = getPacientes();
        try {
            for (int i = 0; i < pacientes.length(); i++) {
                JSONObject paciente = pacientes.getJSONObject(i);
                if (paciente.getString("dni").equals(dni)) {
                    paciente.put("nombre", nombre);
                    paciente.put("apellido", apellido);
                    paciente.put("nacionalidad", nacionalidad);

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
        JSONArray pacientes = getPacientes();
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
        JSONArray pacientes = getPacientes();
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
        JSONArray pacientes = getPacientes();
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

    // Guardar cambios en el archivo JSON
    private void guardar() {
        try {
            Files.write(Paths.get(archivoJson), datos.toString(4).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Prueba rápida
    public static void main(String[] args) {
        ModificarJsonPacientes gestor = new ModificarJsonPacientes("Pacientes.json");

        gestor.agregarPaciente("40000001", "Lucía", "Gómez", "Argentina",
                "San Martín", 1234, "1f", "Buenos Aires", "Buenos Aires");

        gestor.listarPacientes();

        gestor.modificarPaciente("40000001", "Lucía", "Gómez", "Argentina",
                "San Martín", 4321, "2A", "Buenos Aires", "Buenos Aires");

        System.out.println("=== Después de modificar ===");
        gestor.listarPacientes();

        gestor.eliminarPaciente("40000001");

        System.out.println("=== Después de eliminar ===");
        gestor.listarPacientes();
    }
}