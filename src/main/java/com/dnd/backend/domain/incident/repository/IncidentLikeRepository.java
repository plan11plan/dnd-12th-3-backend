package com.dnd.backend.domain.incident.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.incident.entity.IncidentLikeEntity;

@Repository
public interface IncidentLikeRepository {
	List<Object[]> countLikesForAllIncidents();

	int countByIncidentId(Long id);

	void save(IncidentLikeEntity incidentLikeEntity);
}
