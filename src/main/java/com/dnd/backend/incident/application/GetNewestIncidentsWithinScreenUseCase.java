package com.dnd.backend.incident.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.application.response.IncidentCursorResponse;
import com.dnd.backend.incident.dto.IncidentDistanceDto;
import com.dnd.backend.incident.dto.IncidentInfoDto;
import com.dnd.backend.incident.service.IncidentReadService;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetNewestIncidentsWithinScreenUseCase {

	private final IncidentReadService incidentReadService;
	private final IncidentWithMediaAssembler incidentWithMediaAssembler;

	public IncidentCursorResponse execute(
		double topRightX, double topRightY,
		double bottomLeftX, double bottomLeftY,
		double myX, double myY,
		CursorRequest cursorRequest
	) {
		CursorResponse<IncidentInfoDto> incidentCursor = incidentReadService.findIncidentsWithinScreenByCursor(
			topRightX, topRightY, bottomLeftX, bottomLeftY, cursorRequest
		);

		List<IncidentDistanceDto> distanceDtos = incidentCursor.contents().stream()
			.map(incident -> {
				double distance = incidentReadService.calculateDistance(myY, myX, incident.getLatitude(),
					incident.getLongitude());
				return new IncidentDistanceDto(incident.incidentEntity(), incident.editable(), incident.liked(),
					distance);
			})
			.collect(Collectors.toList());

		List<IncidentWithMediaAndDistanceDto> withMediaList = incidentWithMediaAssembler.toIncidentWithMediaAndDistanceDtos(
			distanceDtos);

		return new IncidentCursorResponse(incidentCursor.nextCursorRequest(), withMediaList);
	}
}
