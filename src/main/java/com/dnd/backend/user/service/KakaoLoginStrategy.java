package com.dnd.backend.user.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
public class KakaoLoginStrategy implements SocialLoginStrategy {

	private final UserRepository userRepository;
	private final RestTemplate restTemplate;

	@Override
	public UserPrincipal login(SocialLoginRequest request) throws BadRequestException {
		String accessToken = request.getToken();
		String url = "https://kapi.kakao.com/v2/user/me";

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
		Map<String, Object> response = responseEntity.getBody();

		if (response == null || !response.containsKey("kakao_account")) {
			throw new BadRequestException("Invalid Kakao access token");
		}

		Map<String, Object> kakaoAccount = (Map<String, Object>)response.get("kakao_account");
		String email = (String)kakaoAccount.get("email");

		if (email == null) {
			throw new BadRequestException("Kakao account email not available");
		}

		User user = userRepository.findByEmail(email).orElseGet(() -> {
			User newUser = User.builder()
				.email(email)
				.password("") // 소셜 로그인은 패스워드 미사용
				.socialLoginType(SocialLoginType.KAKAO)
				.build();
			return userRepository.save(newUser);
		});

		return UserPrincipal.create(user);
	}
}
