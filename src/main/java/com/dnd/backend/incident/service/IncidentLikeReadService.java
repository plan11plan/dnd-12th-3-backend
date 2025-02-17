package com.dnd.backend.incident.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.dnd.backend.incident.entity.IncidentLikeRepository;
import com.dnd.backend.incident.entity.LikeStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentLikeReadService {

	private static final String LIKE_COUNT_PREFIX = "incident:like_count:";

	private final IncidentLikeRepository incidentLikeRepository;
	private final StringRedisTemplate redisTemplate;

	/**
	 * Redis에 저장된 좋아요 수를 반환하며, 없으면 DB에서 조회
	 */
	public int getLikeCount(Long incidentId) {
		String likeCountKey = LIKE_COUNT_PREFIX + incidentId;
		String count = redisTemplate.opsForValue().get(likeCountKey);
		if (count != null) {
			return Integer.parseInt(count);
		}
		return incidentLikeRepository.countByIncidentIdAndStatus(incidentId, LikeStatus.LIKE);
	}

	// 사용자가 좋아요를 눌렀는지 확인
	public boolean hasUserLiked(Long userId, Long incidentId) {
		return incidentLikeRepository.existsByUserIdAndIncidentId(userId, incidentId);
	}
}
