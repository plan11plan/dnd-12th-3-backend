package com.dnd.backend.user.service;

import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dnd.backend.user.dto.AuthResponse;
import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.entity.SocialType;
import com.dnd.backend.user.entity.User;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.repository.UserRepository;
import com.dnd.backend.user.security.JwtTokenProvider;
import com.dnd.backend.user.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

	private final UserRepository userRepository;
	private final RestTemplate restTemplate;
	private final JwtTokenProvider tokenProvider;

	@Transactional
	public AuthResponse loginWithGoogle(SocialLoginRequest request) {
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
				.socialType(SocialType.GOOGLE)
				.build();
			return userRepository.save(newUser);
		});

		UserPrincipal principal = UserPrincipal.create(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null,
			principal.getAuthorities());
		String jwt = tokenProvider.generateToken(authentication);
		return new AuthResponse(jwt);
	}

	@Transactional
	public AuthResponse loginWithKakao(SocialLoginRequest request) {
		String accessToken = request.getToken();
		String url = "https://kapi.kakao.com/v2/user/me";

		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
		org.springframework.http.ResponseEntity<Map> responseEntity = restTemplate.exchange(url,
			org.springframework.http.HttpMethod.GET, entity, Map.class);
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
				.socialType(SocialType.KAKAO)
				.build();
			return userRepository.save(newUser);
		});

		UserPrincipal principal = UserPrincipal.create(user);
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null,
			principal.getAuthorities());
		String jwt = tokenProvider.generateToken(authentication);
		return new AuthResponse(jwt);
	}
}
