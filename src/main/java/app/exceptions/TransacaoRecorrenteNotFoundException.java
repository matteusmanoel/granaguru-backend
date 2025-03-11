package app.exceptions;

/**
 * Exceção lançada quando uma transação recorrente não é encontrada.
 */
public class TransacaoRecorrenteNotFoundException extends RuntimeException {
    
    /**
     * Construtor que aceita um ID da transação recorrente.
     * 
     * @param id O ID da transação recorrente não encontrada.
     */
    public TransacaoRecorrenteNotFoundException(Long id) {
        super("Transação Recorrente não encontrada com o ID: " + id);
    }

    /**
     * Construtor que aceita uma mensagem personalizada.
     * 
     * @param message Mensagem personalizada de erro.
     */
    public TransacaoRecorrenteNotFoundException(String message) {
        super(message);
    }
}
