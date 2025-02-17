package com.dnd.backend.incident.entity.category.type;

import com.dnd.backend.incident.entity.category.IncidentCategory;
import com.dnd.backend.incident.entity.category.IncidentType;

public enum NaturalIncidentType implements IncidentType {
	TYPHOON("태풍"),
	DROUGHT("건조"),
	FOREST_FIRE("산불"),
	LANDSLIDE("산사태"),
	FLOOD("홍수"),
	HEAVY_RAIN("호우"),
	HEAT_WAVE("폭염"),
	HEAVY_SNOW("대설"),
	TSUNAMI("지진해일"),
	EARTHQUAKE("지진");

	private final String name;

	NaturalIncidentType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IncidentCategory getParentType() {
		return IncidentCategory.자연재난;
	}
}
