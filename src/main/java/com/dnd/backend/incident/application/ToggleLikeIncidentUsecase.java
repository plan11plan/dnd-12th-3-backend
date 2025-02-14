package com.dnd.backend.incident.application;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.service.IncidentLikeWriteService;
import com.dnd.backend.incident.service.IncidentReadService;
import com.dnd.backend.tobeUser.UserReadService;

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
