package br.com.supermidia.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
		return buildErrorResponse("error", ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
/*
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(
			DataIntegrityViolationException ex) {
		return buildErrorResponse("error", "Erro de integridade referencial ou campo duplicado.", HttpStatus.BAD_REQUEST);
	}
*/
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(
	        DataIntegrityViolationException ex) {

	    String rootCauseMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() 
	                                                        : "Erro de integridade referencial ou campo duplicado.";

	    return buildErrorResponse("error", rootCauseMessage, HttpStatus.BAD_REQUEST);
	}	
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
		// Concatena todas as mensagens de erro de validação
		String errorMessage = ex.getConstraintViolations().stream()
				.map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
				.collect(Collectors.joining(", "));
		return buildErrorResponse("validation_error", errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
				.collect(Collectors.joining(", "));
		return buildErrorResponse("validation_error", errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
		return buildErrorResponse("error","Erro interno no servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
	    return buildErrorResponse("runtime_error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Método utilitário para criar uma resposta JSON
	private ResponseEntity<Map<String, String>> buildErrorResponse(String type, String message, HttpStatus status) {
		Map<String, String> response = new HashMap<>();
		response.put("errorType", type);
		response.put("message", message);
		return ResponseEntity.status(status).body(response);
	}
}
