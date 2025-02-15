package com.dnd.backend.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dnd.backend.user.entity.User;
import com.dnd.backend.user.exception.ResourceNotFoundException;
import com.dnd.backend.user.repository.UserRepository;
import com.dnd.backend.user.security.UserPrincipal;

@Service
public class SecurityService {

	private final UserRepository userRepository;

	public SecurityService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
		return userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}
}
