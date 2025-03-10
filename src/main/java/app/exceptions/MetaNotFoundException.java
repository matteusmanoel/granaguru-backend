package app.exceptions;

public class MetaNotFoundException extends RuntimeException {
    public MetaNotFoundException(Long id) {
        super("Meta não encontrada com o ID: " + id);
    }
}
