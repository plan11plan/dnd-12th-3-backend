package com.dnd.backend.user.service.login;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.entity.SocialLoginType;
import com.dnd.backend.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

	private final MemberRepository memberRepository;

	@Transactional
	public MemberEntity registerNewUser(String providerId, String email, String name,
		SocialLoginType socialLoginType) {
		// 이미 등록된 회원인지 확인
		Optional<MemberEntity> existingMember = memberRepository.findByEmail(email);
		if (existingMember.isPresent()) {
			return existingMember.get(); // 이미 등록된 회원이면 해당 회원 정보 반환
		}

		// 신규 회원 등록
		MemberEntity newMemberEntity = MemberEntity.builder()
			.email(email)
			.name(name)
			.password("") // 소셜 로그인은 패스워드 미사용
			.socialLoginType(socialLoginType)
			.providerId(providerId)
			.build();
		try {
			return memberRepository.save(newMemberEntity);
		} catch (DataIntegrityViolationException ex) {
			throw new IllegalArgumentException("Registration failed due to duplicate email.");
		}
	}
}
