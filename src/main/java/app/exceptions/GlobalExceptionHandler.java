package app.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // === EXCEPTIONS ESPECÍFICAS (NOT FOUND) ===

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

    // === VALIDATIONS (bean validation - @Valid / @Validated) ===

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }

    // === JPA TRANSACTION WRAPPER (ex: falha por causa de Bean Validation em @Transactional) ===

    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTransactionSystemException(TransactionSystemException ex) {
        Throwable cause = ex.getRootCause();
        if (cause instanceof ConstraintViolationException violationEx) {
            StringBuilder message = new StringBuilder("Erro de validação: ");
            for (ConstraintViolation<?> violation : violationEx.getConstraintViolations()) {
                message.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            return message.toString();
        }
        return "Erro de transação. Verifique os dados enviados.";
    }

    // === DATA INTEGRITY ===

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return "Erro de integridade de dados: " + ex.getMostSpecificCause().getMessage();
    }

    // === GENERIC ERROR ===

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllUncaughtException(Exception ex) {
        ex.printStackTrace(); // log para debug
        return "Erro interno: " + ex.getMessage();
    }
}
