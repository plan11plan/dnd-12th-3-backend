package com.dnd.backend.user.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.entity.SocialLoginType;
import com.dnd.backend.user.repository.UserRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	@Autowired
	private UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		Map<String, Object> attributes = oAuth2User.getAttributes();
		System.out.println("OAuth2 Attributes: " + attributes);

		final String email;
		final String name;
		final SocialLoginType socialLoginType;

		if (registrationId.equalsIgnoreCase("kakao")) {
			socialLoginType = SocialLoginType.KAKAO;
			Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
			Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
			email = (String)kakaoAccount.get("email");
			name = (String)kakaoProfile.get("nickname");
		} else if (registrationId.equalsIgnoreCase("google")) {
			socialLoginType = SocialLoginType.GOOGLE;
			email = (String)attributes.get("email");
			name = (String)attributes.get("name");
		} else {
			socialLoginType = SocialLoginType.LOCAL;
			email = null;
			name = null;
		}

		if (email == null) {
			throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
		}

		MemberEntity memberEntity = userRepository.findByEmail(email).orElseGet(() -> {
			MemberEntity newMemberEntity = MemberEntity.builder()
				.email(email)
				.name(name)
				.password("")
				.socialLoginType(socialLoginType)
				.build();
			return userRepository.save(newMemberEntity);
		});

		CustomeUserDetails principal = CustomeUserDetails.create(memberEntity);
		principal.setAttributes(attributes);
		return principal;
	}
}
