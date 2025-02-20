package com.dnd.backend.incident.application;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.application.response.IncidentCursorResponse;
import com.dnd.backend.incident.dto.IncidentInfoDto;
import com.dnd.backend.incident.service.IncidentReadService;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetIncidentsByCursorUseCase {

	private final IncidentReadService incidentReadService;
	private final IncidentWithMediaAssembler incidentWithMediaAssembler;

	public IncidentCursorResponse execute(Long writerId, CursorRequest cursorRequest) {
		// Get incidents with additional info (editable, liked)
		CursorResponse<IncidentInfoDto> incidentCursor = incidentReadService.getIncidents(writerId, cursorRequest);
		var incidentInfoDtos = incidentCursor.contents();

		// Extract IncidentEntity from IncidentInfoDto
		// var incidents = incidentInfoDtos.stream()
		// 	.map(IncidentInfoDto::incidentEntity)
		// 	.collect(Collectors.toList());

		// Assemble IncidentWithMediaDto
		var withMediaList = incidentWithMediaAssembler.toIncidentWithMediaDtos(incidentInfoDtos);

		// Create response with cursor and assembled data
		return new IncidentCursorResponse(incidentCursor.nextCursorRequest(), withMediaList);
	}
}
