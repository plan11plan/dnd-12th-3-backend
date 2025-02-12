package com.dnd.backend.infrastructure.repository.incident;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.incident.entity.IncidentEntity;
import com.dnd.backend.domain.incident.repository.IncidentRepository;
import com.dnd.backend.infrastructure.repository.incident.jpa.IncidentJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class IncidentRepositoryImpl implements IncidentRepository {
	private final IncidentJpaRepository incidentJpaRepository;

	@Override
	public IncidentEntity save(IncidentEntity incidentEntity) {
		return incidentJpaRepository.save(incidentEntity);
	}

	@Override
	public Optional<List<IncidentEntity>> findAllByWriterId(Long writerId, Pageable pageable) {
		var incidentEntities = incidentJpaRepository.findAllByWriterId(writerId, pageable);
		return Optional.of(incidentEntities);
	}

	@Override
	public Optional<List<IncidentEntity>> findAllByWriterIdAndIdLessThan(Long writerId, Long id, Pageable pageable) {
		var incidentEntities = incidentJpaRepository.findAllByWriterIdAndIdLessThan(writerId, id, pageable);
		return Optional.of(incidentEntities);
	}

	@Override
	public Optional<List<IncidentEntity>> findAll() {
		var incidentEntities = incidentJpaRepository.findAll();
		return Optional.of(incidentEntities);
	}
}
