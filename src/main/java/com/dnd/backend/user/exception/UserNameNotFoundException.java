package com.dnd.backend.user.exception;

public class UserNameNotFoundException extends RuntimeException {
	public UserNameNotFoundException(String message) {
		super(message);
	}
}
