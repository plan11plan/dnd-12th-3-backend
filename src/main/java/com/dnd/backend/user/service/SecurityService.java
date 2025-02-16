package com.dnd.backend.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.user.entity.User;
import com.dnd.backend.user.exception.ResourceNotFoundException;
import com.dnd.backend.user.exception.UnauthorizedException;
import com.dnd.backend.user.repository.UserRepository;
import com.dnd.backend.user.security.CustomeUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityService {

	private static final String UNAUTHORIZED_MESSAGE = "유효한 인증 토큰이 필요합니다.";
	private static final String USER_NOT_FOUND_MESSAGE = "사용자를 찾을 수 없습니다.";

	private final UserRepository userRepository;

	public User getAuthenticatedUser() {
		Authentication authentication = getAuthentication();
		validateAuthentication(authentication);

		return getUserFromPrincipal(authentication.getPrincipal());
	}

	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private void validateAuthentication(Authentication authentication) {
		if (authentication == null) {
			throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
		}
	}

	private User getUserFromPrincipal(Object principal) {
		if (!(principal instanceof CustomeUserDetails)) {
			throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
		}

		CustomeUserDetails customeUserDetails = (CustomeUserDetails)principal;
		return userRepository.findById(customeUserDetails.getId())
			.orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE));
	}
}
