package com.dnd.backend.tobeUser;

import org.springframework.http.HttpStatus;

import com.dnd.backend.support.response.code.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
