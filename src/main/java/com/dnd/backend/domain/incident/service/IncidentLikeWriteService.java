package com.dnd.backend.domain.incident.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.domain.incident.entity.IncidentLikeEntity;
import com.dnd.backend.domain.incident.entity.LikeStatus;
import com.dnd.backend.domain.incident.repository.IncidentLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentLikeWriteService {

	private static final String LIKE_COUNT_PREFIX = "incident:like_count:";
	private static final String USER_LIKE_PREFIX = "incident:user_likes:";

	private final IncidentLikeRepository incidentLikeRepository;
	private final StringRedisTemplate redisTemplate;

	@Transactional
	public LikeStatus toggleLike(Long userId, Long incidentId) {
		IncidentLikeEntity likeEntity = incidentLikeRepository
			.findByIncidentIdAndWriterId(incidentId, userId)
			.orElse(null);

		if (isLiked(likeEntity)) {
			return processUnlike(likeEntity, userId, incidentId);
		} else {
			return processLike(likeEntity, userId, incidentId);
		}
	}

	private boolean isLiked(IncidentLikeEntity likeEntity) {
		return likeEntity != null && likeEntity.isLike();
	}

	private LikeStatus processUnlike(IncidentLikeEntity likeEntity, Long userId, Long incidentId) {
		likeEntity.toUnLike();
		incidentLikeRepository.save(likeEntity);
		updateRedisOnUnlike(userId, incidentId);
		return LikeStatus.UNLIKE;
	}

	private LikeStatus processLike(IncidentLikeEntity likeEntity, Long userId, Long incidentId) {
		if (likeEntity == null) {
			likeEntity = IncidentLikeEntity.builder()
				.writerId(userId)
				.incidentId(incidentId)
				.likeStatus(LikeStatus.LIKE)
				.build();
		} else {
			likeEntity.setLikeStatus(LikeStatus.LIKE);
		}
		incidentLikeRepository.save(likeEntity);
		updateRedisOnLike(userId, incidentId);
		return LikeStatus.LIKE;
	}

	private void updateRedisOnLike(Long userId, Long incidentId) {
		String likeCountKey = LIKE_COUNT_PREFIX + incidentId;
		String userLikeKey = USER_LIKE_PREFIX + incidentId;
		redisTemplate.opsForSet().add(userLikeKey, userId.toString());
		redisTemplate.opsForValue().increment(likeCountKey);
		redisTemplate.expire(likeCountKey, 1, TimeUnit.DAYS);
		redisTemplate.expire(userLikeKey, 1, TimeUnit.DAYS);
	}

	private void updateRedisOnUnlike(Long userId, Long incidentId) {
		String likeCountKey = LIKE_COUNT_PREFIX + incidentId;
		String userLikeKey = USER_LIKE_PREFIX + incidentId;
		redisTemplate.opsForSet().remove(userLikeKey, userId.toString());
		redisTemplate.opsForValue().decrement(likeCountKey);
	}
}
