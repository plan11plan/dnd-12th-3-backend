package com.dnd.backend.incident.dto;

import com.dnd.backend.incident.entity.IncidentEntity;

public record IncidentDistanceDto(
	IncidentEntity incident,
	double distance
) {
}
