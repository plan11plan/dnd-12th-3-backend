package com.dnd.backend.application.comment;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dnd.backend.domain.comment.CommentEntity;
import com.dnd.backend.domain.comment.CommentReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCommentsUsecase {

	private final CommentReadService commentReadService;

	public List<CommentEntity> execute(Long incidentId) {
		return commentReadService.getCommentsByIncident(incidentId);
	}
}
