package com.dnd.backend.user.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.user.dto.AuthResponse;
import com.dnd.backend.user.dto.LoginRequest;
import com.dnd.backend.user.dto.RegistrationRequest;
import com.dnd.backend.user.entity.SocialLoginType;
import com.dnd.backend.user.entity.User;
import com.dnd.backend.user.repository.UserRepository;
import com.dnd.backend.user.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;
	private final UserValidator userValidator;

	@Transactional
	public String registerUser(RegistrationRequest request) {
		userValidator.validateEmail(request.getEmail());

		User user = User.builder()
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.socialLoginType(SocialLoginType.LOCAL)
			.build();

		userRepository.save(user);
		return "User registered successfully";
	}

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

	public void logoutUser() {
		// JWT는 상태없으므로 클라이언트에서 토큰 삭제 처리
	}
}
