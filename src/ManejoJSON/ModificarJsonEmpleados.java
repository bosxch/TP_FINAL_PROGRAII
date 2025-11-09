package ManejoJSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ModificarJsonEmpleados {

    private String archivoJson;
    private JSONObject datos;

    public ModificarJsonEmpleados(String archivoJson) {
        this.archivoJson = archivoJson;
        try {
            // Leer archivo si existe, si no crear estructura vacía
            if (Files.exists(Paths.get(archivoJson))) {
                String content = new String(Files.readAllBytes(Paths.get(archivoJson)));
                datos = new JSONObject(content);
            } else {
                datos = new JSONObject();
                datos.put("empleados", new JSONArray());
                guardar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private JSONArray getEmpleados() {
        try {
            return datos.getJSONArray("empleados");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Agregar un nuevo empleado
    public void agregarEmpleado(int id, String nombre, String puesto) {
        try {
        JSONObject nuevoEmpleado = new JSONObject();
        nuevoEmpleado.put("id", id);
        nuevoEmpleado.put("nombre", nombre);
        nuevoEmpleado.put("puesto", puesto);
        getEmpleados().put(nuevoEmpleado);
        guardar();
    } catch (JSONException e) {
        throw new RuntimeException(e);
    }
    }

    // Modificar un empleado existente por id
    public void modificarEmpleado(int id, String nombre, String puesto) {
        JSONArray empleados = getEmpleados();
        for (int i = 0; i < empleados.length(); i++) {
            JSONObject empleado = null;
            try {
                empleado = empleados.getJSONObject(i);

            if (empleado.getInt("id") == id) {
                empleado.put("nombre", nombre);
                empleado.put("puesto", puesto);
                break;
            }

            }
            catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        guardar();
    }

    // Eliminar un empleado por id
    public void eliminarEmpleado(int id) {
        JSONArray empleados = getEmpleados();
        for (int i = 0; i < empleados.length(); i++) {
            JSONObject empleado = null;
            try {
                empleado = empleados.getJSONObject(i);
            if (empleado.getInt("id") == id) {
                empleados.remove(i);
                break;
            }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        guardar();
    }

    // Listar todos los empleados
    public void listarEmpleados() {
        JSONArray empleados = getEmpleados();
        for (int i = 0; i < empleados.length(); i++) {
            try {
                System.out.println(empleados.getJSONObject(i).toString(4));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Obtener un empleado por id
    public JSONObject obtenerEmpleado(int id) {
        JSONArray empleados = getEmpleados();
        for (int i = 0; i < empleados.length(); i++) {
            JSONObject empleado = null;
            try {
                empleado = empleados.getJSONObject(i);
            if (empleado.getInt("id") == id) {
                return empleado;
            }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    // Guardar cambios en el archivo JSON
    private void guardar() {
        try {
            Files.write(Paths.get(archivoJson), datos.toString(4).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método de prueba
    public static void main(String[] args) {
        ModificarJsonEmpleados gestor = new ModificarJsonEmpleados("empleados.json");

        // Agregar empleados
        gestor.agregarEmpleado(1, "Juan", "Administrativo");
        gestor.agregarEmpleado(2, "Ana", "Profesional");

        // Listar
        System.out.println("=== Lista inicial ===");
        gestor.listarEmpleados();

        // Modificar
        gestor.modificarEmpleado(2, "Ana María", "Profesional Senior");

        // Listar
        System.out.println("=== Lista después de modificar ===");
        gestor.listarEmpleados();

        // Eliminar
        gestor.eliminarEmpleado(1);

        // Listar
        System.out.println("=== Lista final ===");
        gestor.listarEmpleados();
    }
}