package com.dnd.backend.comment.application;

import org.springframework.stereotype.Component;

import com.dnd.backend.comment.CommentEntity;
import com.dnd.backend.comment.CommentReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCommentUsecase {

	private final CommentReadService commentReadService;

	public CommentEntity execute(Long incidentId) {
		return commentReadService.getComment(incidentId);
	}
}
