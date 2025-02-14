package com.dnd.backend.domain.incident.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.domain.incident.repository.IncidentLikeRepository;
import com.dnd.backend.domain.incident.repository.IncidentRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class IncidentLikeScheduler {

	private final IncidentLikeRepository incidentLikeRepository;
	private final IncidentRepository incidentRepository;
	private final EntityManager entityManager;

	@Scheduled(fixedRate = 3000) // 3초마다 실행
	@Transactional
	public void updateIncidentLikeCounts() {
		log.info("🛠️ 좋아요 수 업데이트 시작");

		List<Object[]> likeCounts = incidentLikeRepository.countLikesForAllIncidents();

		if (likeCounts.isEmpty()) {
			log.info("ℹ️ 업데이트할 좋아요 데이터가 없습니다.");
			return;
		}

		// Bulk Update
		for (Object[] result : likeCounts) {
			Long incidentId = (Long)result[0];
			int likeCount = ((Number)result[1]).intValue();
			incidentRepository.updateLikeCount(incidentId, likeCount);
		}

		// 영속성 컨텍스트 정리
		entityManager.flush();
		entityManager.clear();

		log.info("✅ 좋아요 수 업데이트 완료");
	}
}
