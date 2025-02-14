package com.dnd.backend.domain.incident.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.dnd.backend.domain.incident.entity.LikeStatus;
import com.dnd.backend.domain.incident.repository.IncidentLikeRepository;
import com.dnd.backend.domain.incident.repository.IncidentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeCountSynchronizer implements ApplicationRunner {
	private static final String LIKE_COUNT_PREFIX = "incident:like_count:";
	private static final String USER_LIKE_PREFIX = "incident:user_likes:";
	private static final int REDIS_TTL_DAYS = 1;

	private final IncidentRepository incidentRepository;
	private final IncidentLikeRepository incidentLikeRepository;
	private final StringRedisTemplate redisTemplate;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("🔄 서버 시작: DB와 Redis 좋아요 동기화 시작");

		clearExistingRedisKeys();
		synchronizeLikeCounts();

		log.info("✅ 서버 시작 동기화 완료");
	}

	private void clearExistingRedisKeys() {
		clearRedisKeysByPrefix(LIKE_COUNT_PREFIX, "LIKE_COUNT");
		clearRedisKeysByPrefix(USER_LIKE_PREFIX, "USER_LIKE");
	}

	private void clearRedisKeysByPrefix(String prefix, String keyType) {
		Set<String> keys = redisTemplate.keys(prefix + "*");
		if (keys != null && !keys.isEmpty()) {
			redisTemplate.delete(keys);
			log.info("Redis {} 키 초기화 완료: {} keys deleted", keyType, keys.size());
		}
	}

	private void synchronizeLikeCounts() {
		var counts = incidentLikeRepository.countLikesGroupedByIncident(LikeStatus.LIKE);

		if (counts == null || counts.isEmpty()) {
			log.info("ℹ️ 동기화할 좋아요 데이터가 DB에 없습니다.");
			return;
		}

		counts.forEach(this::synchronizeIncidentLikes);
	}

	private void synchronizeIncidentLikes(Object[] row) {
		Long incidentId = (Long)row[0];
		int likeCount = ((Number)row[1]).intValue();

		updateIncidentLikeCount(incidentId, likeCount);
		updateRedisLikeCount(incidentId, likeCount);
		reconstructUserLikesList(incidentId);

		log.info("Incident {} 동기화: likeCount = {}", incidentId, likeCount);
	}

	private void updateIncidentLikeCount(Long incidentId, int likeCount) {
		incidentRepository.updateLikeCount(incidentId, likeCount);
	}

	private void updateRedisLikeCount(Long incidentId, int likeCount) {
		redisTemplate.opsForValue()
			.set(LIKE_COUNT_PREFIX + incidentId,
				String.valueOf(likeCount),
				REDIS_TTL_DAYS,
				TimeUnit.DAYS);
	}

	private void reconstructUserLikesList(Long incidentId) {
		var userIds = incidentLikeRepository.findUserIdsByIncidentAndStatus(incidentId, LikeStatus.LIKE);

		if (userIds != null && !userIds.isEmpty()) {
			String userLikeKey = USER_LIKE_PREFIX + incidentId;

			userIds.forEach(userId ->
				redisTemplate.opsForSet().add(userLikeKey, userId.toString())
			);

			redisTemplate.expire(userLikeKey, REDIS_TTL_DAYS, TimeUnit.DAYS);
		}
	}
}
