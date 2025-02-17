package com.dnd.backend.user.service.login;

import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.security.CustomeUserDetails;

public interface SocialAuthStrategy {

	/**
	 * 회원가입: 사용자가 미등록 상태여야 하며, 이미 등록된 경우 예외 처리
	 */
	CustomeUserDetails handleSocialLogin(SocialLoginRequest request);
}
