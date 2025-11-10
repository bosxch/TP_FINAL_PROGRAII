package excepciones;

public class RecetasNoDisponiblesException extends RuntimeException {
    public RecetasNoDisponiblesException(String message) {
        super(message);
    }
}

