package com.dnd.backend.domain.comment.usecase;

import org.springframework.stereotype.Component;

import com.dnd.backend.domain.comment.CommentWriteService;
import com.dnd.backend.domain.incident.entity.IncidentEntity;
import com.dnd.backend.domain.incident.service.IncidentReadService;
import com.dnd.backend.domain.tobeUser.UserEntity;
import com.dnd.backend.domain.tobeUser.UserReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateCommentUsecase {
	private final CommentWriteService commentWriteService;
	private final IncidentReadService incidentReadService;
	private final UserReadService userReadService;

	public void execute(Long incidentId, Long commentId, Long writerId, String content) {
		IncidentEntity incident = incidentReadService.getIncident(incidentId);
		UserEntity user = userReadService.getUser(writerId);
		commentWriteService.updateComment(commentId, content);
	}
}
