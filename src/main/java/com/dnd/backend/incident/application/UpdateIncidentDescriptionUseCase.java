package com.dnd.backend.incident.application;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.dto.UpdateIncidentCommand;
import com.dnd.backend.incident.service.IncidentWriteService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateIncidentDescriptionUseCase {
	private final IncidentWriteService incidentWriteService;

	public void execute(Long incidentId, UpdateIncidentCommand command) {
		incidentWriteService.updateIncidentDetails(incidentId, command);
	}
}
