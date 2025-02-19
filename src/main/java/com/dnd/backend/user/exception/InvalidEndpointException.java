package com.dnd.backend.user.exception;

public class InvalidEndpointException extends RuntimeException {
	public InvalidEndpointException(String message) {
		super(message);
	}
}
