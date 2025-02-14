package com.dnd.backend.incident.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface IncidentLikeRepository {

	void save(IncidentLikeEntity incidentLikeEntity);

	Optional<IncidentLikeEntity> findByIncidentIdAndWriterId(Long incidentId, Long writerId);

	int countByIncidentIdAndStatus(Long incidentId, LikeStatus status);

	List<Object[]> countLikesGroupedByIncident(LikeStatus status);

	List<Long> findUserIdsByIncidentAndStatus(Long incidentId, LikeStatus status);

}
