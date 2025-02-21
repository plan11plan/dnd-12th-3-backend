package com.dnd.backend.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.service.login.AuthResponse;
import com.dnd.backend.user.service.login.SocialRegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class SocialRegistrationController {

	private final SocialRegistrationService socialRegistrationService;

	// Google 소셜 로그인
	@PostMapping("/google")
	public ResponseEntity<AuthResponse> googleRegister(@RequestBody SocialLoginRequest request)
		throws BadRequestException {
		AuthResponse response = socialRegistrationService.handleSocialLogin(request, "google");
		return ResponseEntity.ok(response);
	}

	// Kakao 소셜 로그인
	@PostMapping("/kakao")
	public ResponseEntity<AuthResponse> kakaoRegister(@RequestBody SocialLoginRequest request)
		throws BadRequestException {
		AuthResponse response = socialRegistrationService.handleSocialLogin(request, "kakao");
		return ResponseEntity.ok(response);
	}

	// 긴급 소셜 로그인
	@PostMapping("/token")
	public ResponseEntity<AuthResponse> emerRegister(@RequestBody SocialLoginRequest request)
		throws BadRequestException {
		AuthResponse response = socialRegistrationService.handleSocialLogin(request, "emer");
		return ResponseEntity.ok(response);
	}
}
