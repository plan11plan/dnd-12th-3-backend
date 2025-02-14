package com.dnd.backend.incident.exception;

import com.dnd.backend.support.exception.AppException;

public class InvalidDisasterCategoryException extends AppException {
	public InvalidDisasterCategoryException() {
		super(IncidentErrorCode.INCIDENT_DISASTER_CATEGORY_INVALID_DATA);
	}
}
