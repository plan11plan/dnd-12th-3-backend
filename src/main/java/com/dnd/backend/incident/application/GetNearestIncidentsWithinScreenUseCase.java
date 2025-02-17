package com.dnd.backend.incident.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.dto.IncidentDistanceDto;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.service.IncidentReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetNearestIncidentsWithinScreenUseCase {

	private final IncidentReadService incidentReadService;
	private final IncidentWithMediaAssembler incidentWithMediaAssembler;

	public List<IncidentWithMediaAndDistanceDto> execute(
		double topRightX, double topRightY, // 오른쪽 위 좌표
		double bottomLeftX, double bottomLeftY, // 왼쪽 아래 좌표
		double myX, double myY // 사용자 위치
	) {
		List<IncidentEntity> incidents = incidentReadService.findIncidentsWithinScreen(
			topRightX, topRightY, bottomLeftX, bottomLeftY
		);
		List<IncidentDistanceDto> distanceDtos = incidents.stream()
			.map(incident -> {
				double distance = incidentReadService.calculateDistance(myY, myX, incident.getLatitude(),
					incident.getLongitude());
				return new IncidentDistanceDto(incident, distance);
			})
			.sorted(Comparator.comparingDouble(IncidentDistanceDto::distance))
			.collect(Collectors.toList());
		return incidentWithMediaAssembler.toIncidentWithMediaAndDistanceDtos(distanceDtos);
	}
}
