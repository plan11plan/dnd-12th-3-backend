package com.dnd.backend.incident.dto;

import java.time.LocalDateTime;

import com.dnd.backend.incident.entity.IncidentEntity;

public record IncidentInfoDto(IncidentEntity incident, boolean editable, boolean liked) {
	public Long getId() {
		return incident.getId();
	}

	public IncidentEntity incidentEntity() {
		return incident;
	}

	public IncidentEntity incident() {
		return incident;
	}

	public int getCommentCount() {
		return incident.getCommentCount();
	}

	public double getLatitude() {
		return incident.getLatitude();
	}

	public double getLongitude() {
		return incident.getLongitude();
	}

	public LocalDateTime getCreatedAt() {
		return incident.getCreatedAt();
	}
}
