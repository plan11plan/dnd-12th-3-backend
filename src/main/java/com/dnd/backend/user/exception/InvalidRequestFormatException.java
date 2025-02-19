package com.dnd.backend.user.exception;

public class InvalidRequestFormatException extends RuntimeException {
	public InvalidRequestFormatException(String message) {
		super(message);
	}
}
