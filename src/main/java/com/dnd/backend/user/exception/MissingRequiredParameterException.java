package com.dnd.backend.user.exception;

public class MissingRequiredParameterException extends RuntimeException {
	public MissingRequiredParameterException(String message) {
		super(message);
	}
}
