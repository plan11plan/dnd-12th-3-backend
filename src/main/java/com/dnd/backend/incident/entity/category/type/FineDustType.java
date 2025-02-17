package com.dnd.backend.incident.entity.category.type;

import com.dnd.backend.incident.entity.category.IncidentCategory;
import com.dnd.backend.incident.entity.category.IncidentType;

public enum FineDustType implements IncidentType {
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
	public IncidentCategory getParentType() {
		return IncidentCategory.미세먼지;
	}
}
