package com.dnd.backend.user.entity;

import com.dnd.backend.user.exception.BadRequestException;

public enum SocialLoginType {
	LOCAL,
	GOOGLE,
	KAKAO;

	public static SocialLoginType from(String provider) {
		try {
			return SocialLoginType.valueOf(provider.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("Unsupported social login provider: " + provider);
		}
	}
}
