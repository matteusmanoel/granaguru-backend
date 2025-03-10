package app.exceptions;

public class TransacaoNotFoundException extends RuntimeException {
    public TransacaoNotFoundException(Long id) {
        super("Transação não encontrada com o ID: " + id);
    }
}
