package com.dnd.backend.user.service.oauth;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.entity.SocialLoginType;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
	private final MemberRepository memberRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public MemberEntity registerUserIfNotExists(String email, String name, SocialLoginType socialLoginType) {
		System.out.println("이메일" + email);
		System.out.println("이름" + name);

		Optional<MemberEntity> optionalUser = memberRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		}

		MemberEntity newMemberEntity = MemberEntity.builder()
			.email(email)    // 올바른 순서로 수정
			.name(name)      // 올바른 순서로 수정
			.password("")
			.socialLoginType(socialLoginType)
			.build();

		System.out.println(newMemberEntity.toString());

		try {
			MemberEntity savedMemberEntity = memberRepository.save(newMemberEntity);
			memberRepository.flush();
			return savedMemberEntity;
		} catch (DataIntegrityViolationException ex) {
			return memberRepository.findByEmail(email)
				.orElseThrow(() -> new BadRequestException("MemberEntity registration failed due to duplicate email."));
		}
	}
}
