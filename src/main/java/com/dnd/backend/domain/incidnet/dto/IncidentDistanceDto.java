package com.dnd.backend.domain.incidnet.dto;

import com.dnd.backend.domain.incidnet.entity.IncidentEntity;

public record IncidentDistanceDto(
	IncidentEntity incident,
	double distance
) {
}
