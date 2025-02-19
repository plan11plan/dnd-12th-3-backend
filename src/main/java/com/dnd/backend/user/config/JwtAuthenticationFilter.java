package com.dnd.backend.user.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dnd.backend.user.exception.UnauthorizedException;
import com.dnd.backend.user.exception.token.InvalidClaimException;
import com.dnd.backend.user.exception.token.InvalidTokenException;
import com.dnd.backend.user.exception.token.MalformedTokenException;
import com.dnd.backend.user.exception.token.NotAnIdTokenException;
import com.dnd.backend.user.exception.token.TokenExpiredException;
import com.dnd.backend.user.security.CustomUserDetailsService;
import com.dnd.backend.user.security.CustomeUserDetails;
import com.dnd.backend.user.security.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider tokenProvider;
	private final CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain)
		throws ServletException, IOException {
		try {
			String token = getJwtFromRequest(request);

			// 토큰이 없는 경우
			if (!StringUtils.hasText(token)) {
				throw new UnauthorizedException("인증 토큰이 필요합니다.");
			}

			// 토큰 유효성 검사
			if (!tokenProvider.validateToken(token)) {
				throw new InvalidTokenException("유효하지 않은 토큰입니다.");
			}

			// 토큰에서 userId 추출
			Long userId = tokenProvider.getUserIdFromJWT(token);

			// 토큰이 ID 토큰인지 확인
			if (!tokenProvider.isIdToken(token)) {
				throw new NotAnIdTokenException("ID 토큰이 아닙니다.");
			}

			// 사용자 정보 로드
			UserDetails userDetails = customUserDetailsService.loadUserById(userId);
			if (!(userDetails instanceof CustomeUserDetails)) {
				throw new UnauthorizedException("유효한 인증 토큰이 필요합니다.");
			}

			// SecurityContext에 인증 정보 설정
			CustomeUserDetails principal = (CustomeUserDetails)userDetails;
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				principal, null, principal.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (ExpiredJwtException ex) {
			// 토큰 만료 예외
			throw new TokenExpiredException("토큰이 만료되었습니다.");
		} catch (MalformedJwtException ex) {
			// 토큰 형식 오류
			throw new MalformedTokenException("토큰 형식이 잘못되었습니다.");
		} catch (IllegalArgumentException ex) {
			// 토큰 클레임 오류
			throw new InvalidClaimException("토큰 클레임이 유효하지 않습니다.");
		} catch (Exception ex) {
			// 기타 예외
			throw new UnauthorizedException("인증 중 오류가 발생했습니다: " + ex.getMessage());
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
