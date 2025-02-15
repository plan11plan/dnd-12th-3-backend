package com.dnd.backend.user.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication)
		throws IOException, ServletException {
		String token = tokenProvider.generateToken(authentication);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		Map<String, String> tokenMap = new HashMap<>();
		tokenMap.put("token", token);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), tokenMap);
	}
}
