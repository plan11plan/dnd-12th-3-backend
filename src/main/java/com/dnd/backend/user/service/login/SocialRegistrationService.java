package com.dnd.backend.user.service.login;

import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.security.CustomeUserDetails;
import com.dnd.backend.user.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocialRegistrationService {

	private final Map<String, SocialAuthStrategy> socialAuthStrategies;
	private final JwtTokenProvider tokenProvider;

	public AuthResponse handleSocialLogin(SocialLoginRequest request, String provider) {
		SocialAuthStrategy strategy = socialAuthStrategies.get(provider + "AuthStrategy");
		if (strategy == null) {
			throw new IllegalArgumentException("Unsupported social login provider: " + provider);
		}
		CustomeUserDetails principal = strategy.handleSocialLogin(request);
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null,
			principal.getAuthorities());

		return generateAuthResponse(authentication, principal.getId(), principal.getName());
	}

	private AuthResponse generateAuthResponse(Authentication authentication, Long id, String name) {
		String accessToken = tokenProvider.generateAccessToken(authentication);
		String idToken = tokenProvider.generateIdToken(authentication);
		String refreshToken = tokenProvider.generateRefreshToken(authentication);
		long expiresIn = tokenProvider.getAccessTokenExpiry();
		long refreshTokenExpiresIn = tokenProvider.getRefreshTokenExpiry();
		String scope = tokenProvider.getScope();
		log.info("🔥 jwt 토큰 = {}", accessToken);
		return new AuthResponse(id, name, "bearer", accessToken, idToken, expiresIn, refreshToken,
			refreshTokenExpiresIn, scope);
	}
}
