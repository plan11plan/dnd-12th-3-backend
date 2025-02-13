package com.dnd.backend.domain.incident.entity.category;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DisasterCategoryValidator implements ConstraintValidator<ValidDisasterCategory, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) {
			return false;
		}

		try {
			DisasterCategory.mapToDisasterGroup(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
