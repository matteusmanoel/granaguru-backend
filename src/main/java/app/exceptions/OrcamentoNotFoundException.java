package app.exceptions;

public class OrcamentoNotFoundException extends RuntimeException {
	public OrcamentoNotFoundException(Long id) {
		super("Orçamento não encontrado com o ID: " + id);
	}

	public OrcamentoNotFoundException(String message) {
		super(message);
	}
}