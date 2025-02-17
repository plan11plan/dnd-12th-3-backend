package com.dnd.backend.incident.entity.category;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IncidentCategoryValidator implements ConstraintValidator<ValidIncidentCategory, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) {
			return false;
		}

		try {
			IncidentCategory.mapToDisasterGroup(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
