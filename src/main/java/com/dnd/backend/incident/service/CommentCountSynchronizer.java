package com.dnd.backend.incident.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.dnd.backend.comment.CommentRepository;
import com.dnd.backend.incident.entity.IncidentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentCountSynchronizer implements ApplicationRunner {

	private static final String COMMENT_COUNT_PREFIX = "incident:comment_count:";
	private static final int REDIS_TTL_DAYS = 1;

	private final IncidentRepository incidentRepository;
	private final CommentRepository commentRepository;
	private final StringRedisTemplate redisTemplate;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("🔄 서버 시작: DB와 Redis 댓글 수 동기화 시작");
		clearExistingRedisKeys();
		synchronizeCommentCounts();
		log.info("✅ 서버 시작 댓글 수 동기화 완료");
	}

	private void clearExistingRedisKeys() {
		Set<String> keys = redisTemplate.keys(COMMENT_COUNT_PREFIX + "*");
		if (keys != null && !keys.isEmpty()) {
			redisTemplate.delete(keys);
			log.info("Redis 댓글 수 키 초기화 완료: {} keys deleted", keys.size());
		}
	}

	private void synchronizeCommentCounts() {
		var counts = commentRepository.countCommentsGroupedByIncident();
		if (counts == null || counts.isEmpty()) {
			log.info("DB에 동기화할 댓글 데이터가 없습니다.");
			return;
		}
		counts.forEach(this::synchronizeIncidentComments);
	}

	private void synchronizeIncidentComments(Object[] row) {
		Long incidentId = (Long)row[0];
		int commentCount = ((Number)row[1]).intValue();
		updateIncidentCommentCount(incidentId, commentCount);
		updateRedisCommentCount(incidentId, commentCount);
		log.info("Incident {} 동기화: commentCount = {}", incidentId, commentCount);
	}

	private void updateIncidentCommentCount(Long incidentId, int commentCount) {
		incidentRepository.updateCommentCount(incidentId, commentCount);
	}

	private void updateRedisCommentCount(Long incidentId, int commentCount) {
		String key = COMMENT_COUNT_PREFIX + incidentId;
		redisTemplate.opsForValue().set(key, String.valueOf(commentCount), REDIS_TTL_DAYS, TimeUnit.DAYS);
	}
}
