package com.dnd.backend.user.exception.token;

public class NotAnIdTokenException extends RuntimeException {
	public NotAnIdTokenException(String message) {
		super(message);
	}
}
