package com.dnd.backend.domain.incident.entity.category.type;

import com.dnd.backend.domain.incident.entity.category.DisasterCategory;
import com.dnd.backend.domain.incident.entity.category.DisasterType;

public enum FineDustType implements DisasterType {
	FINE_DUST("미세먼지");

	private final String name;

	FineDustType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DisasterCategory getParentType() {
		return DisasterCategory.미세먼지;
	}
}
