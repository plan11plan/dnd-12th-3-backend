package com.dnd.backend.incident.dto;

public record WriteIncidentCommand(

	String title,

	String description,

	String disasterGroup,

	double pointX,

	double pointY
) {
}
