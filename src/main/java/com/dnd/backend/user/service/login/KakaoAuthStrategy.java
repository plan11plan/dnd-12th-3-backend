package com.dnd.backend.user.service.login;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.entity.SocialLoginType;
import com.dnd.backend.user.repository.MemberRepository;
import com.dnd.backend.user.security.CustomeUserDetails;

import lombok.RequiredArgsConstructor;

@Component("kakaoAuthStrategy")
@RequiredArgsConstructor
public class KakaoAuthStrategy implements SocialAuthStrategy {

	private final RestTemplate restTemplate;
	private final UserRegistrationService userRegistrationService;
	private final MemberRepository memberRepository;

	@Override
	public CustomeUserDetails handleSocialLogin(SocialLoginRequest request) {
		String accessToken = request.getToken();
		Map<String, Object> userInfo = getUserInfo(accessToken);

		String email = extractEmail(userInfo);
		String name = extractName(userInfo);

		// 이미 등록된 사용자라면 해당 사용자 정보 반환
		Optional<MemberEntity> existingMember = memberRepository.findByEmail(email);
		if (existingMember.isPresent()) {
			return CustomeUserDetails.create(existingMember.get());
		}

		// 신규 회원 등록
		MemberEntity memberEntity = userRegistrationService.registerNewUser(request.getToken(), email, name,
			SocialLoginType.KAKAO);
		return CustomeUserDetails.create(memberEntity);
	}

	private Map<String, Object> getUserInfo(String accessToken) {
		String url = "https://kapi.kakao.com/v2/user/me";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
		Map<String, Object> response = responseEntity.getBody();
		if (response == null || !response.containsKey("kakao_account")) {
			throw new IllegalArgumentException("Invalid Kakao access token");
		}
		return (Map<String, Object>)response.get("kakao_account");
	}

	private String extractEmail(Map<String, Object> userInfo) {
		String email = (String)userInfo.get("email");
		if (email == null) {
			// throw new IllegalArgumentException("Kakao account email not available");
			email = "이메일없음요";
		}

		return email.toLowerCase().trim();
	}

	private String extractName(Map<String, Object> userInfo) {
		if (userInfo.containsKey("profile")) {
			@SuppressWarnings("unchecked")
			Map<String, Object> profile = (Map<String, Object>)userInfo.get("profile");
			return (String)profile.get("nickname");
		}
		return null;
	}
}
