package com.dnd.backend.support.presentation.advice;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.dnd.backend.support.response.Response;
import com.dnd.backend.support.response.code.ClientErrorCode;

import jakarta.servlet.http.HttpServletRequest;

@Order(1)
@RestControllerAdvice
public class ResourceNotFoundExceptionHandler {

	@ExceptionHandler(NoResourceFoundException.class)
	public Response<?> handleNoResourceFoundException(
		HttpServletRequest request,
		NoResourceFoundException ex) {
		return Response.fail(
			ClientErrorCode.NOT_FOUND,
			"요청하신 리소스를 찾을 수 없습니다.",
			request.getRequestURI(),
			null
		);
	}
}
