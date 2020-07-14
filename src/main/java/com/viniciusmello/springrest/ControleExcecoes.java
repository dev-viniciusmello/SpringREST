package com.viniciusmello.springrest;

import java.sql.SQLException;
import java.util.List;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {
	
	public ControleExcecoes() {
		// TODO Auto-generated constructor stub
	}
	
//			INTERCEPTA A MAIORIA DOS ERROS COMUNS	

	@ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		String msg = "";
		
		adicionarMensagemErro(msg, ex);
		
		ObjetoErro erro = new ObjetoErro();
		
		erro.setError(msg);
		
		erro.setCode(status.value() + " ==> " + status.getReasonPhrase());
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	
	}

	private void adicionarMensagemErro(String msg, Exception ex) {
		
		if (ex instanceof MethodArgumentNotValidException) {
			
			List<ObjectError> errors = 
			  ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
			
			for (ObjectError x : errors) {
				msg += x.getDefaultMessage() + "\n";
			}
			
		}
		else msg = ex.getMessage();
			
	}
	
//			TRATAMENTO DA MAIORIA DOS ERROS A NIVEL DE BANCO DE DADOS
	
	@ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {
		String msg = "";
		
		if (ex instanceof DataIntegrityViolationException) {
			msg = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage(); 
		}
		else if (ex instanceof ConstraintViolationException) {
			msg = ((ConstraintViolationException) ex).getCause().getCause().getMessage(); 
		}
		else if (ex instanceof SQLException) {
			msg = ((SQLException) ex).getCause().getCause().getMessage(); 
		}
		else {
			msg = ex.getMessage();
		}
		
		ObjetoErro error = new ObjetoErro();
		error.setError(msg);
		error.setCode(HttpStatus.INTERNAL_SERVER_ERROR + " ==> " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		return null;
	}

}
