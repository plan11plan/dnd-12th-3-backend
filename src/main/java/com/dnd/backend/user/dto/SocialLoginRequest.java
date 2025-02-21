package com.dnd.backend.user.dto;

import lombok.Data;

@Data
public class SocialLoginRequest {
	// 구글: ID 토큰, 카카오: Access Token
	// @NotBlank(message = "토큰은 필수 항목입니다.")
	private String token;
	private String name;
	private String email;
}
