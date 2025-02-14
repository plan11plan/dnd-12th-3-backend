package com.dnd.backend.application.comment;

import org.springframework.stereotype.Component;

import com.dnd.backend.domain.comment.CommentEntity;
import com.dnd.backend.domain.comment.CommentReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCommentUsecase {

	private final CommentReadService commentReadService;

	public CommentEntity execute(Long incidentId) {
		return commentReadService.getComment(incidentId);
	}
}
