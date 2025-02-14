package com.dnd.backend.infrastructure.repository.incident;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.incident.entity.IncidentLikeEntity;
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
	public List<Object[]> countLikesForAllIncidents() {
		return incidentLikeJpaRepository.countLikesForAllIncidents();
	}

	@Override
	public int countByIncidentId(Long id) {
		return incidentLikeJpaRepository.countByIncidentId(id);
	}

}
