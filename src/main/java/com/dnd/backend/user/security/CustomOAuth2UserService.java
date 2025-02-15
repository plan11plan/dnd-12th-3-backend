package com.dnd.backend.user.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.dnd.backend.user.entity.SocialType;
import com.dnd.backend.user.entity.User;
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

		// email과 socialType을 final로 선언
		final String email;
		final SocialType socialType;

		if (registrationId.equalsIgnoreCase("kakao")) {
			socialType = SocialType.KAKAO;
			Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
			email = (String)kakaoAccount.get("email");
		} else if (registrationId.equalsIgnoreCase("google")) {
			socialType = SocialType.GOOGLE;
			email = (String)attributes.get("email");
		} else {
			socialType = SocialType.LOCAL;
			email = null;
		}

		if (email == null) {
			throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
		}

		User user = userRepository.findByEmail(email).orElseGet(() -> {
			User newUser = User.builder()
				.email(email)
				.password("")
				.socialType(socialType)
				.build();
			return userRepository.save(newUser);
		});

		UserPrincipal principal = UserPrincipal.create(user);
		principal.setAttributes(attributes);
		return principal;
	}
}
