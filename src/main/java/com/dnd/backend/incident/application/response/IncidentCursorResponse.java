package com.dnd.backend.incident.application.response;

import java.util.List;

import com.dnd.backend.support.util.CursorRequest;

public record IncidentCursorResponse<T>(
	CursorRequest nextCursorRequest,
	List<T> incidents
) {

}
