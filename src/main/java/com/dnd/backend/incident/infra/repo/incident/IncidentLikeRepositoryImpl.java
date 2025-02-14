package com.dnd.backend.incident.infra.repo.incident;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dnd.backend.incident.entity.IncidentLikeEntity;
import com.dnd.backend.incident.entity.IncidentLikeRepository;
import com.dnd.backend.incident.entity.LikeStatus;
import com.dnd.backend.incident.infra.repo.incident.jpa.IncidentLikeJpaRepository;

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
