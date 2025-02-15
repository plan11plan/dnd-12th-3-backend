package com.dnd.backend.user.dto;

import lombok.Data;

@Data
public class SocialLoginRequest {
	// 구글: ID 토큰, 카카오: Access Token
	private String token;
}
