package com.dnd.backend.user.service.oauth;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dnd.backend.user.dto.SocialLoginRequest;
import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.entity.SocialLoginType;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.repository.MemberRepository;
import com.dnd.backend.user.security.CustomeUserDetails;
import com.dnd.backend.user.service.SocialLoginStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoLoginStrategy implements SocialLoginStrategy {

	private final MemberRepository memberRepository;
	private final RestTemplate restTemplate;

	@Override
	@Transactional
	public CustomeUserDetails login(SocialLoginRequest request) throws BadRequestException {
		String accessToken = request.getToken();
		String url = "https://kapi.kakao.com/v2/user/me";

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
		Map<String, Object> response = responseEntity.getBody();

		if (response == null || !response.containsKey("kakao_account")) {
			throw new BadRequestException("Invalid Kakao access token");
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> kakaoAccount = (Map<String, Object>)response.get("kakao_account");
		String email = (String)kakaoAccount.get("email");

		if (email == null) {
			throw new BadRequestException("Kakao account email not available");
		}

		// 이메일 정규화: 소문자 변환 및 trim 처리
		String trimEmail = email.toLowerCase().trim();

		MemberEntity memberEntity;
		try {
			memberEntity = memberRepository.findByEmail(email).orElseGet(() -> {
				MemberEntity newMemberEntity = MemberEntity.builder()
					.email(trimEmail)
					.password("") // 소셜 로그인은 패스워드 미사용
					.socialLoginType(SocialLoginType.KAKAO)
					.build();
				return memberRepository.save(newMemberEntity);
			});
		} catch (DataIntegrityViolationException ex) {
			// 동시성 문제 등으로 이미 등록된 경우, 기존 사용자 조회
			memberEntity = memberRepository.findByEmail(email)
				.orElseThrow(() -> new BadRequestException("MemberEntity registration failed due to duplicate email."));
		}

		return CustomeUserDetails.create(memberEntity);
	}
}
