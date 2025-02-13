package com.dnd.backend.presentation.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dnd.backend.support.response.Response;
import com.dnd.backend.support.response.code.ServerErrorCode;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 예외가 발생했을 때 공통적으로 Response.Fail 로 래핑하는 핸들러
 */
@RestControllerAdvice
public class ApiExceptionHandler {
	/**
	 * 기타 모든 예외 처리
	 */
	@ExceptionHandler(Exception.class)
	ResponseEntity<Response<?>> defaultErrorHandler(HttpServletRequest request, Exception e) {
		return ResponseEntity.ok(
			Response.fail(
				ServerErrorCode.INTERNAL_ERROR,
				"서버 오류가 발생했습니다: " + e.getMessage(),
				null,
				null
			)
		);
	}

}
