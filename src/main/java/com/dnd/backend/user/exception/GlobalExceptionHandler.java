package com.dnd.backend.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Order(1)
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		logger.error("Resource not found: ", ex);
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleBadRequestException(BadRequestException ex, WebRequest request) {
		logger.error("Bad request: ", ex);
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
		logger.error("Unauthorized: ", ex);
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
		logger.error("ResponseStatusException: ", ex);
		return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
	}

	// // 새로운 예외 핸들러 추가
	// @ExceptionHandler(InvalidRequestFormatException.class)
	// public ResponseEntity<?> handleInvalidRequestFormatException(InvalidRequestFormatException ex,
	// 	WebRequest request) {
	// 	logger.error("Invalid request format: ", ex);
	//
	// 	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	// }
	//
	// @ExceptionHandler(MissingRequiredParameterException.class)
	// public ResponseEntity<?> handleMissingRequiredParameterException(MissingRequiredParameterException ex,
	// 	WebRequest request) {
	// 	logger.error("Missing required parameter: ", ex);
	//
	// 	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	// }
	//
	// @ExceptionHandler(InvalidEndpointException.class)
	// public ResponseEntity<?> handleInvalidEndpointException(InvalidEndpointException ex,
	// 	WebRequest request) {
	// 	logger.error("Invalid endpoint: ", ex);
	//
	// 	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	// }
	//
	// @ExceptionHandler(ValidationFailedException.class)
	// public ResponseEntity<?> handleValidationFailedException(ValidationFailedException ex,
	// 	WebRequest request) {
	// 	logger.error("Validation failed: ", ex);
	//
	// 	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	// }

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingServletRequestParameterException(
		MissingServletRequestParameterException ex, WebRequest request) {
		logger.error("Missing servlet request parameter: ", ex);

		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
		logger.error("No handler found: ", ex);

		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
		logger.error("Unhandled exception: ", ex);

		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
