package com.dnd.backend.application.incident;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dnd.backend.domain.incidnet.service.IncidentReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetNearIncidentsUsecase {

	private final IncidentReadService incidentReadService;
	private final IncidentWithMediaAssembler incidentWithMediaAssembler;

	public List<IncidentWithMediaAndDistanceDto> execute(double pointX, double pointY, double radiusInKm) {
		var incidents = incidentReadService.findNearbyIncidents(pointX, pointY, radiusInKm);

		return incidentWithMediaAssembler.toIncidentWithMediaAndDistanceDtos(incidents);
	}
}
