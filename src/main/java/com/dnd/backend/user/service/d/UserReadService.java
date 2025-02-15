package com.dnd.backend.user.service.d;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserReadService {
	private final UserRepositoryV2 userRepositoryV2;

	public UserEntity getUser(Long userId) {
		return userRepositoryV2.findById(userId).orElseThrow(UserNotFoundException::new);
	}
}
