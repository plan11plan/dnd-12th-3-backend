package com.dnd.backend.incident.entity.category.type;

import com.dnd.backend.incident.entity.category.IncidentCategory;
import com.dnd.backend.incident.entity.category.IncidentType;

public enum TrafficType implements IncidentType {
	CONTROL("교통통제"),
	ACCIDENT("교통사고");

	private final String name;

	TrafficType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IncidentCategory getParentType() {
		return IncidentCategory.교통;
	}
}
