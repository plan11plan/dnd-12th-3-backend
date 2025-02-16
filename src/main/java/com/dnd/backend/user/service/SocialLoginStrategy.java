package com.dnd.backend.user.service;

import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.security.CustomeUserDetails;

public interface SocialLoginStrategy {
	CustomeUserDetails login(SocialLoginRequest request) throws BadRequestException;
}
