package com.dnd.backend.incident.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.incident.dto.UpdateIncidentCommand;
import com.dnd.backend.incident.dto.WriteIncidentCommand;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.entity.IncidentRepository;
import com.dnd.backend.incident.entity.category.IncidentCategory;
import com.dnd.backend.incident.exception.IncidentNotFoundException;
import com.dnd.backend.incident.exception.InvalidDescriptionException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentWriteService {
	private final IncidentRepository incidentRepository;
	private final GeocodingService geocodingService;

	@Transactional
	public Long create(Long writerId, WriteIncidentCommand command) {
		var disasterGroup = IncidentCategory.mapToDisasterGroup(command.incidentCategory());

		var roadNameAddress = geocodingService.getRoadNameAddress(command.latitude(), command.longitude());

		var incidentEntity = IncidentEntity.builder()
			.locationInfoName(roadNameAddress)
			.writerId(writerId)
			.description(command.description())
			.locationInfoName(command.locationInfoName())
			.roadNameAddress(command.roadNameAddress())
			.lotNumberAddress(command.lotNumberAddress())
			.incidentCategory(disasterGroup)
			.latitude(command.latitude())
			.longitude(command.longitude())
			.build();

		return incidentRepository.save(incidentEntity).getId();
	}

	@Transactional
	public void updateIncidentDetails(Long incidentId, UpdateIncidentCommand command) {
		IncidentEntity incidentEntity = incidentRepository.findById(incidentId)
			.orElseThrow(IncidentNotFoundException::new);

		if (command.description() == null || command.description().isBlank()) {
			throw new InvalidDescriptionException();
		}

		incidentEntity = incidentEntity.updateDetails(
			command.description(),
			command.locationInfoName(), command.roadNameAddress(), command.locationInfoName(),
			IncidentCategory.mapToDisasterGroup(command.incidentCategory()));
		incidentRepository.save(incidentEntity);
	}

	public void delete(Long incidentId) {
		var incidentEntity = incidentRepository.findById(incidentId)
			.orElseThrow(IncidentNotFoundException::new);
		incidentRepository.deleteById(incidentEntity);
	}
}
