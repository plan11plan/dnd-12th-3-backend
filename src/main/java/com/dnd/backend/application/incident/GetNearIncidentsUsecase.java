package com.dnd.backend.application.incident;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dnd.backend.domain.incidnet.entity.IncidentEntity;
import com.dnd.backend.domain.incidnet.service.IncidentReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetNearIncidentsUsecase {
	private final IncidentReadService incidentReadService;

	public List<IncidentEntity> execute(double pointX, double pointY, double radiusInKm) {
		return incidentReadService.findNearbyIncidents(pointX, pointY, radiusInKm);
	}
}
