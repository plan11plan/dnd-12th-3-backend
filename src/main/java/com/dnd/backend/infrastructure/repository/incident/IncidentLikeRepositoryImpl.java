package com.dnd.backend.infrastructure.repository.incident;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.incident.entity.IncidentLikeEntity;
import com.dnd.backend.domain.incident.entity.LikeStatus;
import com.dnd.backend.domain.incident.repository.IncidentLikeRepository;
import com.dnd.backend.infrastructure.repository.incident.jpa.IncidentLikeJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class IncidentLikeRepositoryImpl implements IncidentLikeRepository {
	private final IncidentLikeJpaRepository incidentLikeJpaRepository;

	public void save(IncidentLikeEntity incidentLikeEntity) {
		incidentLikeJpaRepository.save(incidentLikeEntity);
	}

	@Override
	public Optional<IncidentLikeEntity> findByIncidentIdAndWriterId(Long incidentId, Long writerId) {
		return incidentLikeJpaRepository.findByIncidentIdAndWriterId(incidentId, writerId);
	}

	@Override
	public int countByIncidentIdAndStatus(Long incidentId, LikeStatus status) {
		return incidentLikeJpaRepository.countByIncidentIdAndStatus(incidentId, status);
	}

	@Override
	public List<Object[]> countLikesGroupedByIncident(LikeStatus status) {
		return incidentLikeJpaRepository.countLikesGroupedByIncident(status);
	}

	@Override
	public List<Long> findUserIdsByIncidentAndStatus(Long incidentId, LikeStatus status) {
		return incidentLikeJpaRepository.findUserIdsByIncidentAndStatus(incidentId, status);
	}

}
