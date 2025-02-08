package com.dnd.backend.domain.incidnet.service;

import static com.dnd.backend.domain.incidnet.entity.category.DisasterGroup.*;

import org.springframework.stereotype.Service;

import com.dnd.backend.domain.incidnet.dto.WriteIncidentCommand;
import com.dnd.backend.domain.incidnet.entity.IncidentEntity;
import com.dnd.backend.domain.incidnet.entity.category.DisasterGroup;
import com.dnd.backend.domain.incidnet.repository.JpaIncidentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentWriteService {
	private final JpaIncidentRepository incidentCommandRepository;

	public Long create(WriteIncidentCommand command) {
		DisasterGroup disasterGroup = mapToDisasterGroup(command.disasterGroup());

		var incidentEntity = IncidentEntity.builder()
			.writerId(command.writerId())
			.title(command.description())
			.description(command.description())
			.disasterGroup(disasterGroup)
			.pointX(command.pointX())
			.pointY(command.pointY())
			.build();

		return incidentCommandRepository.save(incidentEntity).getId();
	}
}
