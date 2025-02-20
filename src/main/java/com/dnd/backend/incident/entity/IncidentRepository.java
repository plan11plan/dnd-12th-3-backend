package com.dnd.backend.incident.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

public interface IncidentRepository {
	IncidentEntity save(IncidentEntity incidentEntity);

	Optional<IncidentEntity> findById(Long id);

	Optional<List<IncidentEntity>> findAllByWriterId(Long writerId, Pageable pageable);

	Optional<List<IncidentEntity>> findAllByWriterIdAndIdLessThan(Long writerId, Long id, Pageable pageable);

	Optional<List<IncidentEntity>> findAll();

	void deleteById(IncidentEntity id);

	void updateLikeCount(Long incidentId, int likeCount);

	void updateCommentCount(Long incidentId, int commentCount);

	Optional<List<IncidentEntity>> findAllWithinScreenAndIdLessThan(
		double topRightX, double topRightY,
		double bottomLeftX, double bottomLeftY,
		Long id, Pageable pageable
	);

	Optional<List<IncidentEntity>> findAllWithinScreen(
		double topRightX, double topRightY,
		double bottomLeftX, double bottomLeftY,
		Pageable pageable
	);
}

