package com.dnd.backend.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.backend.user.dto.AuthResponse;
import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.service.SocialLoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class SocialLoginController {
	private final SocialLoginService socialLoginService;

	// 구글 소셜 로그인 (안드로이드가 전달한 ID 토큰)
	@PostMapping("/google")
	public ResponseEntity<?> googleLogin(@RequestBody SocialLoginRequest request) throws BadRequestException {
		AuthResponse response = socialLoginService.login(request, "google");
		return ResponseEntity.ok(response);
	}

	// 카카오 소셜 로그인 (안드로이드가 전달한 Access Token)
	@PostMapping("/kakao")
	public ResponseEntity<?> kakaoLogin(@RequestBody SocialLoginRequest request) throws BadRequestException {
		AuthResponse response = socialLoginService.login(request, "kakao");
		return ResponseEntity.ok(response);
	}
}
