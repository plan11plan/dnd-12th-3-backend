package com.dnd.backend.user.service;

import com.dnd.backend.user.dto.AuthResponse;
import com.dnd.backend.user.dto.LoginRequest;
import com.dnd.backend.user.dto.RegistrationRequest;
import com.dnd.backend.user.dto.SocialLoginRequest;

public interface AuthService {
	String registerUser(RegistrationRequest request);

	AuthResponse loginUser(LoginRequest request);

	void logoutUser();

	// 소셜 로그인 (안드로이드에서 전달받은 토큰을 이용)
	AuthResponse loginWithGoogle(SocialLoginRequest request);

	AuthResponse loginWithKakao(SocialLoginRequest request);
}
