package com.dnd.backend.user.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String jwtSecret;

	// 기존 Access Token 만료 시간 (ms 단위)
	@Value("${jwt.expirationMs}")
	private int jwtExpirationMs;

	// 추가: Access Token 만료 시간 (초 단위, 예: 21599초)
	@Value("${jwt.access-token.expiration-time}")
	private long accessExpirationSec;

	// 추가: Refresh Token 만료 시간 (초 단위, 예: 5183999초)
	@Value("${jwt.refresh-token.expiration-time}")
	private long refreshExpirationSec;

	/**
	 * 기존 generateToken() 메서드 (ms 단위 만료 시간 사용)
	 */
	public String generateToken(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		String subject;
		if (principal instanceof CustomeUserDetails) {
			subject = Long.toString(((CustomeUserDetails)principal).getId());
		} else {
			subject = principal.toString();
		}
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact();
	}

	/**
	 * 새로운 Access Token 생성 (만료시간: accessExpirationSec, 초 단위)
	 */
	public String generateAccessToken(Authentication authentication) {
		String subject = extractSubject(authentication);
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + accessExpirationSec * 1000);

		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact();
	}

	/**
	 * 새로운 ID Token 생성 (만료시간: accessExpirationSec, 초 단위)
	 * 필요한 경우 추가 클레임("type" : "id") 등을 부여할 수 있음
	 */
	public String generateIdToken(Authentication authentication) {
		String subject = extractSubject(authentication);
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + accessExpirationSec * 1000);

		return Jwts.builder()
			.setSubject(subject)
			.claim("type", "id")
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact();
	}

	/**
	 * 새로운 Refresh Token 생성 (만료시간: refreshExpirationSec, 초 단위)
	 */
	public String generateRefreshToken(Authentication authentication) {
		String subject = extractSubject(authentication);
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + refreshExpirationSec * 1000);

		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact();
	}

	/**
	 * Access Token 만료 시간(초 단위) 반환
	 */
	public long getAccessTokenExpiry() {
		return accessExpirationSec;
	}

	/**
	 * Refresh Token 만료 시간(초 단위) 반환
	 */
	public long getRefreshTokenExpiry() {
		return refreshExpirationSec;
	}

	/**
	 * 토큰에 부여된 scope 정보 반환 (필요에 따라 변경 가능)
	 */
	public String getScope() {
		return "profile, account_email";
	}

	/**
	 * Authentication 객체에서 subject 추출 (CustomeUserDetails인 경우 id 사용)
	 */
	private String extractSubject(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof CustomeUserDetails) {
			return Long.toString(((CustomeUserDetails)principal).getId());
		} else {
			return principal.toString();
		}
	}

	/**
	 * 기존 메서드: JWT 토큰에서 사용자 id 추출
	 */
	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser()
			.setSigningKey(jwtSecret)
			.parseClaimsJws(token)
			.getBody();

		return Long.parseLong(claims.getSubject());
	}

	/**
	 * 기존 메서드: JWT 토큰 유효성 검사
	 */
	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			// 로깅 등 추가 처리 가능
		}
		return false;
	}
}
