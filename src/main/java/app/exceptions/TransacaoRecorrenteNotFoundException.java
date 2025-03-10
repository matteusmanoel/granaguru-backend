package app.exceptions;

public class TransacaoRecorrenteNotFoundException extends RuntimeException {
    public TransacaoRecorrenteNotFoundException(Long id) {
        super("Transacao Recorrente não encontrada com o ID: " + id);
    }
}