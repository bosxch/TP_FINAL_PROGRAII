package clases;
import java.time.LocalDate;
public class Antecedentes {
    private String idRegistro;
    private String descripcion;
    private LocalDate fechaRegistro;
    private String tipoAntecedente;

    //CONSTRUCTOR
    public Antecedentes(String idRegistro, String descripcion, String tipoAntecedente) {
        this.idRegistro = idRegistro;
        this.descripcion = descripcion;
        this.tipoAntecedente = tipoAntecedente;
        this.fechaRegistro = LocalDate.now();
    }

    public Antecedentes() {
        this.fechaRegistro = LocalDate.now();
    }

    //GETTERS Y SETTERS
    public String getIdRegistro() {
        return idRegistro;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
    public String getTipoAntecedente() {
        return tipoAntecedente;
    }
    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    public void setTipoAntecedente(String tipoAntecedente) {
        this.tipoAntecedente = tipoAntecedente;
    }

    @Override
    public String toString() {
        return "Antecedente [Tipo=" + tipoAntecedente + ", Descripcion=" + descripcion + ", Fecha=" + fechaRegistro + "]";
    }


}
