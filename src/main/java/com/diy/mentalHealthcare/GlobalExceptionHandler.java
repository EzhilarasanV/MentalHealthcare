package com.diy.mentalHealthcare;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
	
//	@ResponseStatus(HttpStatus.UNAUTHORIZED)
//	@ExceptionHandler(UsernameNotFoundException.class)
//	public void handleUsernameNotFoundException() {
//		
//	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public void handleUserAlreadyExistException() {
		
	}

}
