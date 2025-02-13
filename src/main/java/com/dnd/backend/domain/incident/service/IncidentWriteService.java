package com.dnd.backend.domain.incident.service;

import static com.dnd.backend.domain.incident.entity.category.DisasterGroup.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.domain.incident.dto.UpdateIncidentCommand;
import com.dnd.backend.domain.incident.dto.WriteIncidentCommand;
import com.dnd.backend.domain.incident.entity.IncidentEntity;
import com.dnd.backend.domain.incident.entity.category.DisasterGroup;
import com.dnd.backend.domain.incident.exception.IncidentNotFoundException;
import com.dnd.backend.domain.incident.exception.InvalidDescriptionException;
import com.dnd.backend.domain.incident.repository.IncidentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentWriteService {
	private final IncidentRepository incidentCommandRepository;
	private final GeocodingService geocodingService;

	@Transactional
	public Long create(WriteIncidentCommand command) {
		var disasterGroup = mapToDisasterGroup(command.disasterGroup());

		var roadNameAddress = geocodingService.getRoadNameAddress(command.pointX(), command.pointY());

		var incidentEntity = IncidentEntity.builder()
			.roadNameAddress(roadNameAddress)
			.writerId(command.writerId())
			.description(command.description())
			.disasterGroup(disasterGroup)
			.pointX(command.pointX())
			.pointY(command.pointY())
			.build();

		return incidentCommandRepository.save(incidentEntity).getId();
	}

	@Transactional
	public void updateDescription(Long incidentId, UpdateIncidentCommand command) {
		IncidentEntity incidentEntity = incidentCommandRepository.findById(incidentId)
			.orElseThrow(IncidentNotFoundException::new);

		if (command.newDescription() == null || command.newDescription().isBlank()) {
			throw new InvalidDescriptionException();
		}

		incidentEntity = incidentEntity.updateDetails(command.newDescription(), command.newLocationName(),
			DisasterGroup.mapToDisasterGroup(command.newDisasterGroup()));
		incidentCommandRepository.save(incidentEntity);
	}
}
