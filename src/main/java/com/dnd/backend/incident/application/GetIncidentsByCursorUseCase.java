package com.dnd.backend.incident.application;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.application.response.IncidentCursorResponse;
import com.dnd.backend.incident.service.IncidentReadService;
import com.dnd.backend.support.util.CursorRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetIncidentsByCursorUseCase {

	private final IncidentReadService incidentReadService;
	private final IncidentWithMediaAssembler incidentWithMediaAssembler;

	public IncidentCursorResponse execute(Long writerId, CursorRequest cursorRequest) {

		var incidentCursor = incidentReadService.getIncidents(writerId, cursorRequest);
		var incidents = incidentCursor.contents();

		var withMediaList = incidentWithMediaAssembler.toIncidentWithMediaDtos(incidents);

		return new IncidentCursorResponse(incidentCursor.nextCursorRequest(), withMediaList);
	}
}
