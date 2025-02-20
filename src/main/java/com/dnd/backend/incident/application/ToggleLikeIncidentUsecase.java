package com.dnd.backend.incident.application;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.service.IncidentLikeWriteService;
import com.dnd.backend.incident.service.IncidentReadService;
import com.dnd.backend.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ToggleLikeIncidentUsecase {
	private final MemberService memberService;
	private final IncidentReadService incidentReadService;
	private final IncidentLikeWriteService incidentLikeWriteService;

	public String execute(Long userId, Long incidentId) {
		var incident = incidentReadService.getIncident(incidentId);
		var member = memberService.getMember(userId);
		return incidentLikeWriteService.toggleLike(member.getId(), incident.getId()).toString();
	}
}
