package com.dnd.backend.incident.infra.repo.incident;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.entity.IncidentRepository;
import com.dnd.backend.incident.infra.repo.incident.jpa.IncidentJpaRepository;

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
	public Optional<IncidentEntity> findById(Long id) {
		return incidentJpaRepository.findById(id);
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

	@Override
	public void deleteById(IncidentEntity incidentEntity) {
		incidentJpaRepository.delete(incidentEntity);
	}

	@Override
	public void updateLikeCount(Long incidentId, int likeCount) {
		incidentJpaRepository.updateLikeCount(incidentId, likeCount);
	}

	@Override
	public void updateCommentCount(Long incidentId, int commentCount) {
		incidentJpaRepository.updateCommentCount(incidentId, commentCount);
	}

	@Override
	public Optional<List<IncidentEntity>> findAllWithinScreenAndIdLessThan(double topRightX, double topRightY,
		double bottomLeftX, double bottomLeftY, Long id, Pageable pageable) {
		var allWithinScreenAndIdLessThan = incidentJpaRepository.findAllWithinScreenAndIdLessThan(
			topRightX, topRightY, bottomLeftX, bottomLeftY, id, pageable
		);
		return Optional.of(allWithinScreenAndIdLessThan);
	}

	@Override
	public Optional<List<IncidentEntity>> findAllWithinScreen(double topRightX, double topRightY, double bottomLeftX,
		double bottomLeftY, Pageable pageable) {
		var allWithinScreen = incidentJpaRepository.findAllWithinScreen(topRightX, topRightY,
			bottomLeftX, bottomLeftY, pageable);
		return Optional.of(allWithinScreen);
	}

}
