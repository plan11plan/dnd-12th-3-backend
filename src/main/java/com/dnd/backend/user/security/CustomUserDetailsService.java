package com.dnd.backend.user.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.exception.UserNameNotFoundException;
import com.dnd.backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UserNameNotFoundException {
		MemberEntity memberEntity = userRepository.findByEmail(email)
			.orElseThrow(() -> new UserNameNotFoundException("MemberEntity not found with email: " + email));
		return CustomeUserDetails.create(memberEntity);
	}

	public UserDetails loadUserById(Long id) {
		MemberEntity memberEntity = userRepository.findById(id)
			.orElseThrow(() -> new UserNameNotFoundException("MemberEntity not found with id: " + id));
		return CustomeUserDetails.create(memberEntity);
	}
}
