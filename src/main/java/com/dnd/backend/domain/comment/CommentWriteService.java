package com.dnd.backend.domain.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentWriteService {

	private final CommentRepository commentRepository;

	@Transactional
	public CommentEntity createComment(Long incidentId, Long writerId, String content) {
		CommentEntity comment = CommentEntity.builder()
			.incidentId(incidentId)
			.writerId(writerId)
			.content(content)
			.build();
		return commentRepository.save(comment);
	}

	@Transactional
	public CommentEntity createReply(Long incidentId, Long writerId, String content, Long parentId) {
		CommentEntity parent = commentRepository.findById(parentId)
			.orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
		CommentEntity reply = CommentEntity.builder()
			.incidentId(incidentId)
			.writerId(writerId)
			.content(content)
			.parent(parent)
			.build();
		return commentRepository.save(reply);
	}

	@Transactional
	public CommentEntity updateComment(Long commentId, String newContent) {
		CommentEntity comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
		comment.updateContent(newContent);
		return commentRepository.save(comment);
	}

	@Transactional
	public void deleteComment(Long commentId) {
		CommentEntity comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
		commentRepository.delete(comment);
	}
}
