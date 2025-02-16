package com.dnd.backend.user.service;

import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.security.UserPrincipal;

public interface SocialLoginStrategy {
	UserPrincipal login(SocialLoginRequest request) throws BadRequestException;
}
