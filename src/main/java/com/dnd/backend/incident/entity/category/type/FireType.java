package com.dnd.backend.incident.entity.category.type;

import com.dnd.backend.incident.entity.category.IncidentCategory;
import com.dnd.backend.incident.entity.category.IncidentType;

public enum FireType implements IncidentType {
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
	public IncidentCategory getParentType() {
		return IncidentCategory.화재;
	}
}
