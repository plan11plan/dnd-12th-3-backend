package com.dnd.backend.comment.application;

import org.springframework.stereotype.Component;

import com.dnd.backend.comment.CommentWriteService;
import com.dnd.backend.incident.service.IncidentReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteCommentUsecase {
	private final CommentWriteService commentWriteService;
	private final IncidentReadService incidentReadService;
	// private final UserReadService userReadService;

	public void execute(Long incidentId, Long commentId) {
		var incident = incidentReadService.getIncident(incidentId);
		// var memberEntity = userReadService.getMemberEntity(writerId);
		commentWriteService.deleteComment(commentId);
	}
}
