package com.dnd.backend.user.service.login;

import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.exception.UnauthorizedException;
import com.dnd.backend.user.exception.token.InvalidClaimException;
import com.dnd.backend.user.exception.token.MalformedTokenException;
import com.dnd.backend.user.exception.token.TokenExpiredException;
import com.dnd.backend.user.security.CustomeUserDetails;
import com.dnd.backend.user.security.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
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
		try {
			return generateAuthResponse(authentication, principal.getId(), principal.getName());
		} catch (ExpiredJwtException ex) {
			// 토큰 만료 예외
			throw new TokenExpiredException("토큰이 만료되었습니다.");
		} catch (MalformedJwtException ex) {
			// 토큰 형식 오류
			throw new MalformedTokenException("토큰 형식이 잘못되었습니다.");
		} catch (IllegalArgumentException ex) {
			// 토큰 클레임 오류
			throw new InvalidClaimException("토큰 클레임이 유효하지 않습니다.");
		} catch (Exception ex) {
			// 기타 예외
			throw new UnauthorizedException("인증 중 오류가 발생했습니다: " + ex.getMessage());
		}
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
