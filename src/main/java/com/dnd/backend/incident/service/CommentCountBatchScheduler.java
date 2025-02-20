package com.dnd.backend.incident.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.comment.CommentRepository;
import com.dnd.backend.incident.entity.IncidentRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentCountBatchScheduler {
	private static final String COMMENT_COUNT_PREFIX = "incident:comment_count:";
	private static final int REDIS_TTL_DAYS = 1;
	private static final long BATCH_INTERVAL_MS = 5000;

	private final StringRedisTemplate redisTemplate;
	private final IncidentRepository incidentRepository;
	private final CommentRepository commentRepository;
	private final EntityManager entityManager;

	@Scheduled(fixedRate = BATCH_INTERVAL_MS)
	@Transactional
	public void updateIncidentCommentCounts() {
		log.info("🛠️ 배치 동기화: Redis → DB 댓글 수 업데이트 시작");

		Set<String> redisKeys = getRedisCommentCountKeys();

		if (hasRedisKeys(redisKeys)) {
			synchronizeFromRedisToDb(redisKeys);
		} else {
			synchronizeFromDbToRedis();
		}

		clearEntityManager();
		log.info("✅ 배치 동기화 완료: IncidentEntity의 commentCount가 업데이트되었습니다.");
	}

	private Set<String> getRedisCommentCountKeys() {
		return redisTemplate.keys(COMMENT_COUNT_PREFIX + "*");
	}

	private boolean hasRedisKeys(Set<String> keys) {
		return keys != null && !keys.isEmpty();
	}

	private void synchronizeFromRedisToDb(Set<String> keys) {
		keys.forEach(this::updateCommentCountFromRedis);
	}

	private void updateCommentCountFromRedis(String key) {
		Long incidentId = extractIncidentId(key);
		int commentCount = getCommentCountFromRedis(key);
		updateIncidentCommentCount(incidentId, commentCount);
	}

	private Long extractIncidentId(String key) {
		return Long.parseLong(key.replace(COMMENT_COUNT_PREFIX, ""));
	}

	private int getCommentCountFromRedis(String key) {
		String countStr = redisTemplate.opsForValue().get(key);
		return countStr != null ? Integer.parseInt(countStr) : 0;
	}

	private void synchronizeFromDbToRedis() {
		List<Object[]> counts = commentRepository.countCommentsGroupedByIncident();
		counts.forEach(this::synchronizeIncidentData);
	}

	private void synchronizeIncidentData(Object[] row) {
		Long incidentId = (Long)row[0];
		int commentCount = ((Number)row[1]).intValue();

		updateIncidentCommentCount(incidentId, commentCount);
		updateRedisCommentCount(incidentId, commentCount);
	}

	private void updateIncidentCommentCount(Long incidentId, int commentCount) {
		incidentRepository.updateCommentCount(incidentId, commentCount);
	}

	private void updateRedisCommentCount(Long incidentId, int commentCount) {
		String key = COMMENT_COUNT_PREFIX + incidentId;
		redisTemplate.opsForValue()
			.set(key, String.valueOf(commentCount), REDIS_TTL_DAYS, TimeUnit.DAYS);
	}

	private void clearEntityManager() {
		entityManager.flush();
		entityManager.clear();
	}
}
