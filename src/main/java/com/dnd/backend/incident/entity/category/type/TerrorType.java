package com.dnd.backend.incident.entity.category.type;

import com.dnd.backend.incident.entity.category.IncidentCategory;
import com.dnd.backend.incident.entity.category.IncidentType;

public enum TerrorType implements IncidentType {
	RIOT("폭동"),
	TERROR("테러");

	private final String name;

	TerrorType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IncidentCategory getParentType() {
		return IncidentCategory.테러;
	}
}
