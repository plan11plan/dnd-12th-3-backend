package com.dnd.backend.support.response;

import static com.dnd.backend.support.response.Response.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.dnd.backend.support.response.code.ErrorCode;
import com.dnd.backend.support.response.code.ResponseCode;

public sealed interface Response<T> permits Success, Fail {

	static <T> Fail<T> fail(ErrorCode code, String message, T data, List<ErrorDetail> errors) {
		return new Fail<>(code, message, data, errors, LocalDateTime.now());
	}

	static Fail<Void> fail(ErrorCode code, String message) {
		return new Fail<>(code, message, null, null, LocalDateTime.now());
	}

	static Fail<Map<String, String>> failWithErrors(ErrorCode code, Map<String, String> errors) {
		List<ErrorDetail> errorDetails = errors.entrySet().stream()
			.map(entry -> new ErrorDetail(entry.getKey(), entry.getValue()))
			.toList();
		return new Fail<>(code, null, null, errorDetails, LocalDateTime.now());
	}

	static <T> Success<T> success(ResponseCode code, T data) {
		return new Success<>(code, data, LocalDateTime.now());
	}

	record Success<T>(ResponseCode code, T data, LocalDateTime timestamp) implements Response<T> {
		public Success(ResponseCode code, T data) {
			this(code, data, LocalDateTime.now());
		}
	}

	record Fail<T>(ErrorCode code, String message, T data, List<ErrorDetail> errors, LocalDateTime timestamp)
		implements Response<T> {
		public Fail(ErrorCode code, String message, T data, List<ErrorDetail> errors) {
			this(code, message, data, errors, LocalDateTime.now());
		}
	}

	record ErrorDetail(String field, String message) {
	}
}
