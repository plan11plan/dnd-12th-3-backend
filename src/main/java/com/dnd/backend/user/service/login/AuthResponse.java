package com.dnd.backend.user.service.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	private String tokenType;            // 예: "bearer"
	private String accessToken;          // JWT Access Token
	private String idToken;              // ID Token (예: OpenID Connect의 id_token)
	private long expiresIn;              // Access Token 만료시간(초)
	private String refreshToken;         // Refresh Token
	private long refreshTokenExpiresIn;  // Refresh Token 만료시간(초)
	private String scope;                // 토큰 권한 정보 (예: "profile, account_email")
}
