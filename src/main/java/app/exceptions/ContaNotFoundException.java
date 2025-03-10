package app.exceptions;

public class ContaNotFoundException extends RuntimeException {
    public ContaNotFoundException(Long id) {
        super("Conta não encontrada com o ID: " + id);
    }
    public ContaNotFoundException(String message) {
        super(message);
    }
}