package com.dnd.backend.domain.incident.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.domain.incident.entity.IncidentLikeEntity;
import com.dnd.backend.domain.incident.entity.LikeStatus;
import com.dnd.backend.domain.incident.repository.IncidentLikeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IncidentLikeWriteService {

	private final IncidentLikeRepository incidentLikeRepository;

	@Transactional
	public void like(Long writerId, Long incidentId) {
		incidentLikeRepository.save(IncidentLikeEntity.builder()
			.writerId(writerId)
			.incidentId(incidentId)
			.likeStatus(LikeStatus.LIKE)
			.build());
	}
}
