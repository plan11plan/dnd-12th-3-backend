package com.dnd.backend.domain.incidnet.service;

import static com.dnd.backend.domain.incidnet.entity.category.DisasterGroup.*;

import org.springframework.stereotype.Service;

import com.dnd.backend.domain.incidnet.dto.WriteIncidentCommand;
import com.dnd.backend.domain.incidnet.entity.IncidentEntity;
import com.dnd.backend.domain.incidnet.repository.JpaIncidentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentWriteService {
	private final JpaIncidentRepository incidentCommandRepository;
	private final GeocodingService geocodingService;

	public Long create(WriteIncidentCommand command) {
		var disasterGroup = mapToDisasterGroup(command.disasterGroup());

		var roadNameAddress = geocodingService.getRoadNameAddress(command.pointX(), command.pointY());

		var incidentEntity = IncidentEntity.builder()
			.writerId(command.writerId())
			.description(command.description())
			.disasterGroup(disasterGroup)
			.pointX(command.pointX())
			.pointY(command.pointY())
			.roadNameAddress(roadNameAddress)
			.build();

		return incidentCommandRepository.save(incidentEntity).getId();
	}
}
