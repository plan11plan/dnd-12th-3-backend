package com.dnd.backend.user.service;

import org.springframework.stereotype.Component;

import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator {

	private final MemberRepository memberRepository;

	public void validateEmail(String email) {
		if (memberRepository.findByEmail(email).isPresent()) {
			throw new BadRequestException("Email address already in use.");
		}
	}
}
