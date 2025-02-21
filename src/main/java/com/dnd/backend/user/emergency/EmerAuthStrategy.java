package com.dnd.backend.user.emergency;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.entity.SocialLoginType;
import com.dnd.backend.user.repository.MemberRepository;
import com.dnd.backend.user.security.CustomeUserDetails;
import com.dnd.backend.user.service.login.SocialAuthStrategy;
import com.dnd.backend.user.service.login.UserRegistrationService;

import lombok.RequiredArgsConstructor;

@Component("emerAuthStrategy")
@RequiredArgsConstructor
public class EmerAuthStrategy implements SocialAuthStrategy {

	private final RestTemplate restTemplate;
	private final UserRegistrationService userRegistrationService;
	private final MemberRepository memberRepository;

	@Override
	public CustomeUserDetails handleSocialLogin(SocialLoginRequest request) {
		// String idToken = request.getToken();
		// Map<String, Object> userInfo = getUserInfo(idToken);

		String email = request.getEmail();
		String name = request.getName();
		// 이미 등록된 사용자라면 해당 사용자 정보 반환
		Optional<MemberEntity> existingMember = memberRepository.findByEmail(email);
		if (existingMember.isPresent()) {
			return CustomeUserDetails.create(existingMember.get());
		}

		// 신규 회원 등록
		MemberEntity memberEntity = userRegistrationService.registerNewUser(request.getToken(), email, name,
			SocialLoginType.GOOGLE);
		return CustomeUserDetails.create(memberEntity);
	}

	private Map<String, Object> getUserInfo(String idToken) {
		String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
		Map<String, Object> response = restTemplate.getForObject(url, Map.class);
		if (response == null || !response.containsKey("email")) {
			throw new IllegalArgumentException("Invalid Google ID token");
		}
		return response;
	}

	private String extractEmail(Map<String, Object> userInfo) {
		return ((String)userInfo.get("email")).toLowerCase().trim();
	}

	private String extractName(Map<String, Object> userInfo) {
		return (String)userInfo.get("name");
	}
}


