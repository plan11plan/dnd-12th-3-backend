package com.dnd.backend.domain.tobeUser;

import com.dnd.backend.support.exception.AppException;

public class UserNotFoundException extends AppException {
	public UserNotFoundException() {
		super(UserErrorCode.USER_NOT_FOUND);
	}
}
