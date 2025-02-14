package com.dnd.backend.incident.entity.category;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = DisasterCategoryValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDisasterCategory {
	String message() default "유효하지 않은 재난 카테고리입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
