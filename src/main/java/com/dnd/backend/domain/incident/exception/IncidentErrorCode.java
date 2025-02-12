package com.dnd.backend.domain.incident.exception;

import org.springframework.http.HttpStatus;

import com.dnd.backend.support.response.code.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IncidentErrorCode implements ErrorCode {
	INCIDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "사건/사고를 찾을 수 없습니다."),
	INCIDENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사건/사고입니다."),
	INCIDENT_INVALID_DATA(HttpStatus.BAD_REQUEST, "사건/사고 데이터가 유효하지 않습니다."),
	INCIDENT_UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "사건/사고에 접근할 권한이 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
