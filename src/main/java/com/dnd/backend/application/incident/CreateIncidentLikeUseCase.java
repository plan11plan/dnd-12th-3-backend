package com.dnd.backend.application.incident;

import org.springframework.stereotype.Service;

import com.dnd.backend.domain.incident.service.IncidentLikeWriteService;
import com.dnd.backend.domain.incident.service.IncidentReadService;
import com.dnd.backend.domain.tobeUser.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateIncidentLikeUseCase {
	private final IncidentReadService incidentReadService;
	private final UserReadService userReadService;
	private final IncidentLikeWriteService incidentLikeWriteService;

	public void execute(Long incidentId, Long userId) {
		var incident = incidentReadService.getIncident(incidentId);
		var user = userReadService.getUser(userId);
		// incidentLikeWriteService.create(incident, user);

	}
}

