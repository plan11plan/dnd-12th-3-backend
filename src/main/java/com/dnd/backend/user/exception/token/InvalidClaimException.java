package com.dnd.backend.user.exception.token;

public class InvalidClaimException extends RuntimeException {
	public InvalidClaimException(String message) {
		super(message);
	}
}
