package com.dnd.backend.user.service;

import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.dnd.backend.user.dto.AuthResponse;
import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.security.CustomeUserDetails;
import com.dnd.backend.user.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

	private final Map<String, SocialLoginStrategy> socialLoginStrategies;
	private final JwtTokenProvider tokenProvider;

	public AuthResponse login(SocialLoginRequest request, String provider) throws BadRequestException {
		SocialLoginStrategy strategy = socialLoginStrategies.get(provider + "LoginStrategy");
		validateNull(provider, strategy);

		CustomeUserDetails principal = strategy.login(request);
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null,
			principal.getAuthorities());
		String jwt = tokenProvider.generateToken(authentication);
		System.out.println(jwt);
		return new AuthResponse(jwt);
	}

	private void validateNull(String provider, SocialLoginStrategy strategy) {
		if (strategy == null) {
			throw new BadRequestException("Unsupported social login provider: " + provider);
		}
	}
}
