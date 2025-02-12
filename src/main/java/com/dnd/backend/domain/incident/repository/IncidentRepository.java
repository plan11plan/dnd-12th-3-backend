package com.dnd.backend.domain.incident.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.dnd.backend.domain.incident.entity.IncidentEntity;

public interface IncidentRepository {
	IncidentEntity save(IncidentEntity incidentEntity);

	Optional<List<IncidentEntity>> findAllByWriterId(Long writerId, Pageable pageable);

	Optional<List<IncidentEntity>> findAllByWriterIdAndIdLessThan(Long writerId, Long id, Pageable pageable);

	Optional<List<IncidentEntity>> findAll();
}
