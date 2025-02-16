package com.dnd.backend.user.service.oauth;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.entity.SocialLoginType;
import com.dnd.backend.user.entity.User;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.security.CustomeUserDetails;
import com.dnd.backend.user.service.SocialLoginStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleLoginStrategy implements SocialLoginStrategy {

	private final RestTemplate restTemplate;
	private final UserRegistrationService userRegistrationService; // 별도 서비스 주입

	@Override
	public CustomeUserDetails login(SocialLoginRequest request) throws BadRequestException {
		String idToken = request.getToken();
		String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
		Map<String, Object> response = restTemplate.getForObject(url, Map.class);

		if (response == null || !response.containsKey("email")) {
			throw new BadRequestException("Invalid Google ID token");
		}

		// 이메일 정규화: 소문자 변환 및 trim 처리
		String email = ((String)response.get("email")).toLowerCase().trim();
		String name = (String)response.get("name");

		// 별도의 트랜잭션을 통해 사용자 등록을 시도
		User user = userRegistrationService.registerUserIfNotExists(email, name, SocialLoginType.GOOGLE);

		return CustomeUserDetails.create(user);
	}
}
