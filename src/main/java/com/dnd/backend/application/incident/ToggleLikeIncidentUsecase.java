package com.dnd.backend.application.incident;

import org.springframework.stereotype.Component;

import com.dnd.backend.domain.incident.service.IncidentLikeWriteService;
import com.dnd.backend.domain.incident.service.IncidentReadService;
import com.dnd.backend.domain.tobeUser.UserReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ToggleLikeIncidentUsecase {
	private final UserReadService userReadService;
	private final IncidentReadService incidentReadService;
	private final IncidentLikeWriteService incidentLikeWriteService;

	public String execute(Long userId, Long incidentId) {
		var incident = incidentReadService.getIncident(incidentId);
		var user = userReadService.getUser(userId);
		return incidentLikeWriteService.toggleLike(user.getId(), incident.getId()).toString();

	}
}
