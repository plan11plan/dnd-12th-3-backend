package com.dnd.backend.application.incident;

import org.springframework.stereotype.Component;

import com.dnd.backend.domain.incident.service.IncidentWriteService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteIncidentUseCase {
	private final IncidentWriteService incidentWriteService;

	public void execute(Long incidentId) {
		incidentWriteService.delete(incidentId);
	}
}
