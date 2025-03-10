package app.exceptions;

public class NotificacaoNotFoundException extends RuntimeException {
	public NotificacaoNotFoundException(Long id) {
		super("Notificação não encontrada com o ID: " + id);
	}

	public NotificacaoNotFoundException(String message) {
		super(message);
	}
}