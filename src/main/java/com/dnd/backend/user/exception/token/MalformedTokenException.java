package com.dnd.backend.user.exception.token;

public class MalformedTokenException extends RuntimeException {
	public MalformedTokenException(String message) {
		super(message);
	}
}
