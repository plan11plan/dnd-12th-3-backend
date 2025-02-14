package com.dnd.backend.incident.exception;

public class DuplicateLikeException extends RuntimeException {
	public DuplicateLikeException() {
		super("이미 좋아요를 누른 사건사고입니다.");
	}
}
