package com.dnd.backend.user.service.oauth;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.user.entity.SocialLoginType;
import com.dnd.backend.user.entity.User;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
	private final UserRepository userRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public User registerUserIfNotExists(String email, String name, SocialLoginType socialLoginType) {
		System.out.println("이메일" + email);
		System.out.println("이름" + name);

		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		}

		User newUser = User.builder()
			.email(email)    // 올바른 순서로 수정
			.name(name)      // 올바른 순서로 수정
			.password("")
			.socialLoginType(socialLoginType)
			.build();

		System.out.println(newUser.toString());

		try {
			User savedUser = userRepository.save(newUser);
			userRepository.flush();
			return savedUser;
		} catch (DataIntegrityViolationException ex) {
			return userRepository.findByEmail(email)
				.orElseThrow(() -> new BadRequestException("User registration failed due to duplicate email."));
		}
	}
}
