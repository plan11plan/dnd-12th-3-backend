package com.dnd.backend.comment.application;

import org.springframework.stereotype.Component;

import com.dnd.backend.comment.CommentWriteService;
import com.dnd.backend.incident.service.IncidentReadService;
import com.dnd.backend.tobeUser.UserReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateCommentReplyUsecase {
	private final CommentWriteService commentWriteService;
	private final IncidentReadService incidentReadService;
	private final UserReadService userReadService;

	public void execute(Long incidentId, Long writerId, String content, Long parentId) {
		var incident = incidentReadService.getIncident(incidentId);
		var user = userReadService.getUser(writerId);
		commentWriteService.createReply(incident.getId(), user.getId(), content, parentId);
	}
}
