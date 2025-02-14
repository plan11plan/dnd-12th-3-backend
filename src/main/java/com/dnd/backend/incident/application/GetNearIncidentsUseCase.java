package com.dnd.backend.incident.application;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.service.IncidentReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetNearIncidentsUseCase {

	private final IncidentReadService incidentReadService;
	private final IncidentWithMediaAssembler incidentWithMediaAssembler;

	public List<IncidentWithMediaAndDistanceDto> execute(double pointX, double pointY, double radiusInKm) {
		var incidents = incidentReadService.findNearbyIncidents(pointX, pointY, radiusInKm);

		return incidentWithMediaAssembler.toIncidentWithMediaAndDistanceDtos(incidents);
	}
}
