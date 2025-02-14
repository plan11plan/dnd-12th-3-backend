package com.dnd.backend.infrastructure.repository.incident.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dnd.backend.domain.incident.entity.IncidentLikeEntity;

public interface IncidentLikeJpaRepository extends JpaRepository<IncidentLikeEntity, Long> {
	@Query("SELECT COUNT(l) FROM IncidentLikeEntity l WHERE l.incidentId = :incidentId AND l.likeStatus = 'LIKE'")
	int countByIncidentId(@Param("incidentId") Long incidentId);

	@Query("SELECT l.incidentId, COUNT(l) FROM IncidentLikeEntity l WHERE l.likeStatus = 'LIKE' GROUP BY l.incidentId")
	List<Object[]> countLikesForAllIncidents();
}
