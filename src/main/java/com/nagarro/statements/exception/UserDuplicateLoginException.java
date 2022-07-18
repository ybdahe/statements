package com.nagarro.statements.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserDuplicateLoginException extends RuntimeException {

	public UserDuplicateLoginException(String exception) {
		super(exception);
	}

}
