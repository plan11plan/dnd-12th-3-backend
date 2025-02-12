package com.dnd.backend.domain.incident.dto;

public record UpdateIncidentCommand(
	Long incidentId,
	String newDescription
) {
}
