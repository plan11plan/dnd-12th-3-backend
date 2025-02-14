package com.dnd.backend.comment.application;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dnd.backend.comment.CommentEntity;
import com.dnd.backend.comment.CommentReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCommentsUsecase {

	private final CommentReadService commentReadService;

	public List<CommentEntity> execute(Long incidentId) {
		return commentReadService.getCommentsByIncident(incidentId);
	}
}
