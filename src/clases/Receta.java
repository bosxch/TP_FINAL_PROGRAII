package clases;
import java.time.LocalDate;

public class Receta {
    private String idReceta;
    private String medicamento;
    private String dosis;
    private LocalDate fechaEmision;

    public Receta(String idReceta, String medicamento, String dosis) {
        this.idReceta = idReceta;
        this.medicamento = medicamento;
        this.dosis = dosis;
        this.fechaEmision = LocalDate.now(); // Fecha actual
    }

    public Receta() {
        this.fechaEmision = LocalDate.now();
    }

    public String getIdReceta() {
        return idReceta;
    }
    public String getMedicamento() {
        return medicamento;
    }
    public String getDosis() {
        return dosis;
    }
    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setIdReceta(String idReceta) {
        this.idReceta = idReceta;
    }
    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }
    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    @Override
    public String toString() {
        return "Receta [Medicamento=" + medicamento + ", Dosis=" + dosis + ", Fecha=" + fechaEmision + "]";
    }
}
