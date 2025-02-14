package com.dnd.backend.domain.comment;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentUseCase {

	private final CommentService commentService;

	public CommentEntity createReply(Long incidentId, Long writerId, String content, Long parentId) {
		return commentService.createReply(incidentId, writerId, content, parentId);
	}

	public CommentEntity updateComment(Long commentId, String newContent) {
		return commentService.updateComment(commentId, newContent);
	}

	public void deleteComment(Long commentId) {
		commentService.deleteComment(commentId);
	}

	public CommentEntity getComment(Long commentId) {
		return commentService.getComment(commentId);
	}

	public List<CommentEntity> getCommentsByIncident(Long incidentId) {
		return commentService.getCommentsByIncident(incidentId);
	}
}
