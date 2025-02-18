package com.dnd.backend.comment.application;

import org.springframework.stereotype.Component;

import com.dnd.backend.comment.CommentWriteService;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.service.IncidentReadService;
import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateCommentUsecase {
	private final CommentWriteService commentWriteService;
	private final IncidentReadService incidentReadService;
	private final MemberService memberService;

	public void execute(Long incidentId, Long commentId, Long writerId, String content) {
		IncidentEntity incident = incidentReadService.getIncident(incidentId);
		MemberEntity member = memberService.getMember(writerId);
		commentWriteService.updateComment(commentId, content);
	}
}
