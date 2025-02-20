package com.dnd.backend.comment;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentWriteService {

	private static final String COMMENT_COUNT_PREFIX = "incident:comment_count:";

	private final CommentRepository commentRepository;
	private final StringRedisTemplate redisTemplate;

	@Transactional
	public CommentEntity createComment(Long incidentId, Long writerId, String content) {
		CommentEntity comment = CommentEntity.builder()
			.incidentId(incidentId)
			.writerId(writerId)
			.content(content)
			.build();
		CommentEntity savedComment = commentRepository.save(comment);
		updateRedisOnCreate(incidentId);
		return savedComment;
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
		CommentEntity savedReply = commentRepository.save(reply);
		updateRedisOnCreate(incidentId);
		return savedReply;
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
		updateRedisOnDelete(comment.getIncidentId());
	}

	private void updateRedisOnCreate(Long incidentId) {
		String key = COMMENT_COUNT_PREFIX + incidentId;
		redisTemplate.opsForValue().increment(key);
		redisTemplate.expire(key, 1, TimeUnit.DAYS);
	}

	private void updateRedisOnDelete(Long incidentId) {
		String key = COMMENT_COUNT_PREFIX + incidentId;
		redisTemplate.opsForValue().decrement(key);
		redisTemplate.expire(key, 1, TimeUnit.DAYS);
	}
}
