package com.dnd.backend.user.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dnd.backend.user.dto.AuthResponse;
import com.dnd.backend.user.dto.LoginRequest;
import com.dnd.backend.user.dto.RegistrationRequest;
import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.entity.SocialType;
import com.dnd.backend.user.entity.User;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.repository.UserRepository;
import com.dnd.backend.user.security.JwtTokenProvider;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	@Transactional
	public String registerUser(RegistrationRequest request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new BadRequestException("Email address already in use.");
		}
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setSocialType(SocialType.LOCAL);
		userRepository.save(user);
		return "User registered successfully";
	}

	@Override
	public AuthResponse loginUser(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				request.getEmail(),
				request.getPassword()
			)
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		return new AuthResponse(jwt);
	}

	@Override
	public void logoutUser() {
		// JWT는 상태없으므로 클라이언트에서 토큰 삭제 처리
	}

	@Override
	@Transactional
	public AuthResponse loginWithGoogle(SocialLoginRequest request) {
		String idToken = request.getToken();
		// 구글 ID 토큰 검증 (간단 예제)
		String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
		Map<String, Object> response;
		try {
			response = restTemplate.getForObject(url, Map.class);
		} catch (Exception e) {
			throw new BadRequestException("Invalid Google ID token");
		}

		if (response == null || !response.containsKey("email")) {
			throw new BadRequestException("Invalid Google ID token response");
		}

		String email = (String)response.get("email");
		String name = (String)response.get("name");

		// 해당 이메일의 사용자가 없으면 신규 가입 (소셜 타입: GOOGLE)
		User user = userRepository.findByEmail(email).orElseGet(() -> {
			User newUser = new User();
			newUser.setEmail(email);
			newUser.setName(name);
			newUser.setPassword(""); // 소셜 로그인은 패스워드 미사용
			newUser.setSocialType(SocialType.GOOGLE);
			return userRepository.save(newUser);
		});

		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, null);
		String jwt = tokenProvider.generateToken(authentication);
		return new AuthResponse(jwt);
	}

	@Override
	@Transactional
	public AuthResponse loginWithKakao(SocialLoginRequest request) {
		String accessToken = request.getToken();
		String url = "https://kapi.kakao.com/v2/user/me";
		try {
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

			// 해당 이메일의 사용자가 없으면 신규 가입 (소셜 타입: KAKAO)
			User user = userRepository.findByEmail(email).orElseGet(() -> {
				User newUser = new User();
				newUser.setEmail(email);
				newUser.setPassword(""); // 소셜 로그인은 패스워드 미사용
				newUser.setSocialType(SocialType.KAKAO);
				return userRepository.save(newUser);
			});

			Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, null);
			String jwt = tokenProvider.generateToken(authentication);
			return new AuthResponse(jwt);

		} catch (Exception e) {
			throw new BadRequestException("Invalid Kakao access token");
		}
	}
}
