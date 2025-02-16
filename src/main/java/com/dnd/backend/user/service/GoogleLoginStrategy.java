package com.dnd.backend.user.service;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.entity.SocialLoginType;
import com.dnd.backend.user.entity.User;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.repository.UserRepository;
import com.dnd.backend.user.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleLoginStrategy implements SocialLoginStrategy {

	private final UserRepository userRepository;
	private final RestTemplate restTemplate;

	@Override
	public UserPrincipal login(SocialLoginRequest request) throws BadRequestException {
		String idToken = request.getToken();
		String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
		Map<String, Object> response = restTemplate.getForObject(url, Map.class);

		if (response == null || !response.containsKey("email")) {
			throw new BadRequestException("Invalid Google ID token");
		}

		String email = (String)response.get("email");
		String name = (String)response.get("name");

		User user = userRepository.findByEmail(email).orElseGet(() -> {
			User newUser = User.builder()
				.email(email)
				.name(name)
				.password("") // 소셜 로그인은 패스워드 미사용
				.socialLoginType(SocialLoginType.GOOGLE)
				.build();
			return userRepository.save(newUser);
		});

		return UserPrincipal.create(user);
	}
}
