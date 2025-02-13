package com.dnd.backend.presentation.advice;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dnd.backend.domain.incident.exception.IncidentErrorCode;
import com.dnd.backend.domain.incident.exception.IncidentNotFoundException;
import com.dnd.backend.domain.incident.exception.InvalidDisasterCategoryException;
import com.dnd.backend.support.response.Response;
import com.dnd.backend.support.response.code.ClientErrorCode;

import jakarta.servlet.http.HttpServletRequest;

@Order(1)
@RestControllerAdvice
public class IncidentApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Response<?> handleValidationExceptions(HttpServletRequest request,
		MethodArgumentNotValidException ex) {
		List<Response.ErrorDetail> errors = ex.getBindingResult().getFieldErrors()
			.stream()
			.map(error -> new Response.ErrorDetail(error.getField(), error.getDefaultMessage()))
			.collect(Collectors.toList());

		return Response.fail(ClientErrorCode.VALIDATION_ERROR, "입력값 검증 실패", null, errors);
	}

	@ExceptionHandler(InvalidDisasterCategoryException.class)
	public Response<?> handleInvalidDisasterCategoryException(HttpServletRequest request,
		InvalidDisasterCategoryException ex) {
		return Response.fail(
			IncidentErrorCode.INCIDENT_DISASTER_CATEGORY_INVALID_DATA,
			"존재하지 않는 재난 카테고리입니다.",
			null,
			List.of(new Response.ErrorDetail("disasterGroup", "존재하는 카테고리를 입력해주세요."))
		);
	}

	@ExceptionHandler(IncidentNotFoundException.class)
	public Response<?> handleIncidentNotFoundException(HttpServletRequest request,
		IncidentNotFoundException ex) {
		return Response.fail(
			IncidentErrorCode.INCIDENT_NOT_FOUND,
			ex.getMessage()
		);
	}
}
