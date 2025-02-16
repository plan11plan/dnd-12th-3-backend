package com.dnd.backend.user.security.customAuthenticationPrincipal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthUser {
	// 인증 객체가 올바르지 않은 타입일 경우 오류를 던질지 여부 (기본 true)
	boolean errorOnInvalidType() default true;
}
