package com.nagarro.statements.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotAuthorizedException extends RuntimeException {

	public UserNotAuthorizedException(String exception) {
		super(exception);
	}

}
