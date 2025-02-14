package com.dnd.backend.incident.entity.category.type;

import com.dnd.backend.incident.entity.category.DisasterCategory;
import com.dnd.backend.incident.entity.category.DisasterType;

public enum CollapseType implements DisasterType {
	EMERGENCY("비상");

	private final String name;

	CollapseType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DisasterCategory getParentType() {
		return DisasterCategory.붕괴;
	}
}
