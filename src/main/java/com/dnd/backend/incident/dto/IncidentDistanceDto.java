package com.dnd.backend.incident.dto;

import com.dnd.backend.incident.entity.IncidentEntity;

public record IncidentDistanceDto(
	IncidentEntity incident,
	boolean editable,  // 추가
	boolean liked,      // 추가
	double distance
) {

}
