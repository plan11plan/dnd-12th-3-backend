package com.dnd.backend.user.security.customAuthenticationPrincipal;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.dnd.backend.user.exception.UnauthorizedException;
import com.dnd.backend.user.security.CustomeUserDetails;

public class CustomAuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new UnauthorizedException("인증 정보가 존재하지 않습니다. 유효한 인증 토큰이 필요합니다.");
		}
		Object principal = authentication.getPrincipal();
		// 만약 principal이 null이거나 익명 사용자("anonymousUser")인 경우
		if (principal == null || "anonymousUser".equals(principal)) {
			throw new UnauthorizedException("유효한 인증 토큰이 필요합니다.");
		}
		// principal이 UserPrincipal 타입인지 확인
		if (!(principal instanceof CustomeUserDetails)) {
			AuthUser authUserAnnotation = parameter.getParameterAnnotation(AuthUser.class);
			if (authUserAnnotation != null && authUserAnnotation.errorOnInvalidType()) {
				throw new UnauthorizedException("인증 타입이 올바르지 않습니다.");
			}
			return null;
		}
		return principal;
	}
}
