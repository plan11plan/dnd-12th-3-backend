package com.dnd.backend.domain.tobeUser;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserReadService {
	private final UserRepository userRepository;

	public UserEntity getUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
	}
}
