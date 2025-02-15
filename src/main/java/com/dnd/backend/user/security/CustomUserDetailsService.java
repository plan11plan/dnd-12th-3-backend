package com.dnd.backend.user.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.dnd.backend.user.entity.User;
import com.dnd.backend.user.exception.UserNameNotFoundException;
import com.dnd.backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UserNameNotFoundException {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UserNameNotFoundException("User not found with email: " + email));
		return UserPrincipal.create(user);
	}

	public UserDetails loadUserById(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new UserNameNotFoundException("User not found with id: " + id));
		return UserPrincipal.create(user);
	}
}
