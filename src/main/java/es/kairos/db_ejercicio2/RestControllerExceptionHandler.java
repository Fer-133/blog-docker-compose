package es.kairos.db_ejercicio2;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler(NoSuchElementException.class)
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		
		return handleExceptionInternal(ex, "", new HttpHeaders(), HttpStatus.NOT_FOUND, request);
 }
}
