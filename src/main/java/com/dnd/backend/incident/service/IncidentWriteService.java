package com.dnd.backend.incident.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.incident.dto.UpdateIncidentCommand;
import com.dnd.backend.incident.dto.WriteIncidentCommand;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.entity.IncidentRepository;
import com.dnd.backend.incident.entity.category.DisasterCategory;
import com.dnd.backend.incident.exception.IncidentNotFoundException;
import com.dnd.backend.incident.exception.InvalidDescriptionException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentWriteService {
	private final IncidentRepository incidentRepository;
	private final GeocodingService geocodingService;

	@Transactional
	public Long create(WriteIncidentCommand command) {
		var disasterGroup = DisasterCategory.mapToDisasterGroup(command.disasterGroup());

		var roadNameAddress = geocodingService.getRoadNameAddress(command.pointX(), command.pointY());

		var incidentEntity = IncidentEntity.builder()
			.roadNameAddress(roadNameAddress)
			.writerId(command.writerId())
			.description(command.description())
			.disasterCategory(disasterGroup)
			.pointX(command.pointX())
			.pointY(command.pointY())
			.build();

		return incidentRepository.save(incidentEntity).getId();
	}

	@Transactional
	public void updateIncidentDetails(Long incidentId, UpdateIncidentCommand command) {
		IncidentEntity incidentEntity = incidentRepository.findById(incidentId)
			.orElseThrow(IncidentNotFoundException::new);

		if (command.toDescription() == null || command.toDescription().isBlank()) {
			throw new InvalidDescriptionException();
		}

		incidentEntity = incidentEntity.updateDetails(command.toDescription(), command.toLocationName(),
			DisasterCategory.mapToDisasterGroup(command.toDisasterCategory()));
		incidentRepository.save(incidentEntity);
	}

	public void delete(Long incidentId) {
		var incidentEntity = incidentRepository.findById(incidentId)
			.orElseThrow(IncidentNotFoundException::new);
		incidentRepository.deleteById(incidentEntity);
	}
}
