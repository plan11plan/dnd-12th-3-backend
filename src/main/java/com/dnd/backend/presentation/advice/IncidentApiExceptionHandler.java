package com.dnd.backend.presentation.advice;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dnd.backend.support.response.Response;
import com.dnd.backend.support.response.code.ClientErrorCode;

import jakarta.servlet.http.HttpServletRequest;

@Order(1)
@RestControllerAdvice
public class IncidentApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Response<?> handleValidationExceptions(HttpServletRequest request,
		MethodArgumentNotValidException ex) {
		Map<String, String> errors = ex.getBindingResult().getFieldErrors()
			.stream()
			.collect(Collectors.toMap(
				error -> error.getField(),
				error -> error.getDefaultMessage(),
				(existing, replacement) -> existing
			));

		return Response.failValidation(ClientErrorCode.VALIDATION_ERROR, "부적절한 입력값", errors);
	}
}
