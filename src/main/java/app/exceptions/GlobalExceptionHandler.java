package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UsuarioNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleUsuarioNotFoundException(UsuarioNotFoundException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(TransacaoNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleTransacaoNotFoundException(TransacaoNotFoundException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(TransacaoRecorrenteNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleTransacaoRecorrenteNotFoundException(TransacaoRecorrenteNotFoundException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(OrcamentoNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleOrcamentoNotFoundException(OrcamentoNotFoundException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(NotificacaoNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNotificacaoNotFoundException(NotificacaoNotFoundException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(MetaNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleMetaNotFoundException(MetaNotFoundException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(ContaNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleContaNotFoundException(ContaNotFoundException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(CategoriaNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleCategoriaNotFoundException(CategoriaNotFoundException ex) {
		return ex.getMessage();
	}

}
