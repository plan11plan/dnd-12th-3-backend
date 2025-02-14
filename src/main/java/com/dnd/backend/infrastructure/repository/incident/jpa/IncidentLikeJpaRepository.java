package com.dnd.backend.infrastructure.repository.incident.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dnd.backend.domain.incident.entity.IncidentLikeEntity;
import com.dnd.backend.domain.incident.entity.LikeStatus;

public interface IncidentLikeJpaRepository extends JpaRepository<IncidentLikeEntity, Long> {
	Optional<IncidentLikeEntity> findByIncidentIdAndWriterId(Long incidentId, Long writerId);

	@Query("SELECT COUNT(i) FROM IncidentLikeEntity i WHERE i.incidentId = :incidentId AND i.likeStatus = :status")
	int countByIncidentIdAndStatus(@Param("incidentId") Long incidentId, @Param("status") LikeStatus status);

	@Query("SELECT i.incidentId, COUNT(i) FROM IncidentLikeEntity i WHERE i.likeStatus = :status GROUP BY i.incidentId")
	List<Object[]> countLikesGroupedByIncident(@Param("status") LikeStatus status);

	@Query("SELECT i.writerId FROM IncidentLikeEntity i WHERE i.incidentId = :incidentId AND i.likeStatus = :status")
	List<Long> findUserIdsByIncidentAndStatus(@Param("incidentId") Long incidentId, @Param("status") LikeStatus status);
}
