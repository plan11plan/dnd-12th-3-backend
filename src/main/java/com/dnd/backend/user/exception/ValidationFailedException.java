package com.dnd.backend.user.exception;

public class ValidationFailedException extends RuntimeException {
	public ValidationFailedException(String message) {
		super(message);
	}
}
