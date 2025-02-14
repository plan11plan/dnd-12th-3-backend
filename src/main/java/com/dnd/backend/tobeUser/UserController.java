package com.dnd.backend.tobeUser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	private final UserRepository userRepository;

	@GetMapping
	public void create() {
		userRepository.save(new UserEntity());
	}
}
