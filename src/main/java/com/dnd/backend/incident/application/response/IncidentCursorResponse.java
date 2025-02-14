package com.dnd.backend.incident.application.response;

import java.util.List;

import com.dnd.backend.support.util.CursorRequest;

public record IncidentCursorResponse(
	CursorRequest nextCursorRequest,
	List<IncidentWithMediaDto> incidents
) {

}
