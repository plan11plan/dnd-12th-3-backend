package com.dnd.backend.user.service;

import org.springframework.stereotype.Component;

import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator {

	private final UserRepository userRepository;

	public void validateEmail(String email) {
		if (userRepository.findByEmail(email).isPresent()) {
			throw new BadRequestException("Email address already in use.");
		}
	}
}
