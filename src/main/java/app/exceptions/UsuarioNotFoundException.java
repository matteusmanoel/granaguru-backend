package app.exceptions;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Long id) {
        super("Usuário não encontrado com o ID: " + id);
    }

    // ✅ Construtor adicional para aceitar uma mensagem personalizada
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
