package com.dnd.backend.presentation.controller.incident;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.backend.domain.incidnet.dto.WriteIncidentCommand;
import com.dnd.backend.domain.incidnet.entity.IncidentEntity;
import com.dnd.backend.domain.incidnet.service.IncidentReadService;
import com.dnd.backend.domain.incidnet.service.IncidentWriteService;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incidents")
public class IncidentController {
	private final IncidentWriteService incidentWriteService;
	private final IncidentReadService incidentReadService;

	@PostMapping
	public IncidentEntity write(@RequestBody WriteIncidentCommand command) {
		return incidentWriteService.create(command);
	}

	@GetMapping("/writers/{writerId}")
	public CursorResponse<IncidentEntity> getIncidents(
		@PathVariable("writerId") Long writerId,
		@ModelAttribute CursorRequest cursorRequest
	) {
		return incidentReadService.getIncidents(writerId, cursorRequest);
	}
}
