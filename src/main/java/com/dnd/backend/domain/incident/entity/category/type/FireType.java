package com.dnd.backend.domain.incident.entity.category.type;

import com.dnd.backend.domain.incident.entity.category.DisasterCategory;
import com.dnd.backend.domain.incident.entity.category.DisasterType;

public enum FireType implements DisasterType {
	NATURAL("자연");

	private final String name;

	FireType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DisasterCategory getParentType() {
		return DisasterCategory.화재;
	}
}
