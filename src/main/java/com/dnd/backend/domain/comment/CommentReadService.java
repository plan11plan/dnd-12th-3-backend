package com.dnd.backend.domain.comment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentReadService {
	private final CommentRepository commentRepository;

	@Transactional(readOnly = true)
	public CommentEntity getComment(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
	}

	@Transactional(readOnly = true)
	public List<CommentEntity> getCommentsByIncident(Long incidentId) {
		return commentRepository.findByIncidentIdAndParentIsNullOrderByCreatedAtAsc(incidentId);
	}
}
