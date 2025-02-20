package com.dnd.backend.incident.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.incident.entity.IncidentLikeRepository;
import com.dnd.backend.incident.entity.IncidentRepository;
import com.dnd.backend.incident.entity.LikeStatus;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class IncidentLikeBatchScheduler {
	public static final int REDIS_TTL_DAYS = 1;
	private static final String LIKE_COUNT_PREFIX = "incident:like_count:";
	private static final String USER_LIKE_PREFIX = "incident:user_likes:";
	private static final long BATCH_INTERVAL_MS = 5000;

	private final StringRedisTemplate redisTemplate;
	private final IncidentRepository incidentRepository;
	private final IncidentLikeRepository incidentLikeRepository;
	private final EntityManager entityManager;

	@Scheduled(fixedRate = BATCH_INTERVAL_MS)
	@Transactional
	public void updateIncidentLikeCounts() {
		log.info("🛠️ 배치 동기화: Redis → DB 좋아요 수 업데이트 시작");

		Set<String> redisKeys = getRedisLikeCountKeys();

		if (hasRedisKeys(redisKeys)) {
			synchronizeFromRedisToDb(redisKeys);
		} else {
			synchronizeFromDbToRedis();
		}

		clearEntityManager();
		log.info("✅ 배치 동기화 완료: IncidentEntity의 likeCount가 업데이트되었습니다.");
	}

	private Set<String> getRedisLikeCountKeys() {
		return redisTemplate.keys(LIKE_COUNT_PREFIX + "*");
	}

	private boolean hasRedisKeys(Set<String> keys) {
		return keys != null && !keys.isEmpty();
	}

	private void synchronizeFromRedisToDb(Set<String> keys) {
		keys.forEach(this::updateLikeCountFromRedis);
	}

	private void updateLikeCountFromRedis(String key) {
		Long incidentId = extractIncidentId(key);
		int likeCount = getLikeCountFromRedis(key);
		updateIncidentLikeCount(incidentId, likeCount);
	}

	private Long extractIncidentId(String key) {
		return Long.parseLong(key.replace(LIKE_COUNT_PREFIX, ""));
	}

	private int getLikeCountFromRedis(String key) {
		String countStr = redisTemplate.opsForValue().get(key);
		return countStr != null ? Integer.parseInt(countStr) : 0;
	}

	private void synchronizeFromDbToRedis() {
		List<Object[]> counts = incidentLikeRepository.countLikesGroupedByIncident(LikeStatus.LIKE);
		counts.forEach(this::synchronizeIncidentData);
	}

	private void synchronizeIncidentData(Object[] row) {
		Long incidentId = (Long)row[0];
		int likeCount = ((Number)row[1]).intValue();

		updateIncidentLikeCount(incidentId, likeCount);
		updateRedisLikeCount(incidentId, likeCount);
		reconstructUserLikeSet(incidentId);
	}

	private void updateIncidentLikeCount(Long incidentId, int likeCount) {
		incidentRepository.updateLikeCount(incidentId, likeCount);
	}

	private void updateRedisLikeCount(Long incidentId, int likeCount) {
		String key = LIKE_COUNT_PREFIX + incidentId;
		redisTemplate.opsForValue()
			.set(key, String.valueOf(likeCount), REDIS_TTL_DAYS, TimeUnit.DAYS);
	}

	private void reconstructUserLikeSet(Long incidentId) {
		List<Long> userIds = incidentLikeRepository
			.findUserIdsByIncidentAndStatus(incidentId, LikeStatus.LIKE);

		if (userIds != null && !userIds.isEmpty()) {
			String userLikeKey = USER_LIKE_PREFIX + incidentId;

			userIds.forEach(userId ->
				redisTemplate.opsForSet().add(userLikeKey, userId.toString())
			);

			redisTemplate.expire(userLikeKey, REDIS_TTL_DAYS, TimeUnit.DAYS);
		}
	}

	private void clearEntityManager() {
		entityManager.flush();
		entityManager.clear();
	}
}
