package com.dnd.backend.incident.entity.category.type;

import com.dnd.backend.incident.entity.category.DisasterCategory;
import com.dnd.backend.incident.entity.category.DisasterType;

public enum TrafficType implements DisasterType {
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
	public DisasterCategory getParentType() {
		return DisasterCategory.교통;
	}
}
