package app.exceptions;

public class CategoriaNotFoundException extends RuntimeException {
	public CategoriaNotFoundException(Long id) {
		super("Categoria não encontrada com o ID: " + id);
	}

	public CategoriaNotFoundException(String message) {
		super(message);
	}
}