package com.dnd.backend.presentation.controller.incident;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dnd.backend.application.incident.CreateIncidentLikeUseCase;
import com.dnd.backend.application.incident.CreateIncidentUseCase;
import com.dnd.backend.application.incident.DeleteIncidentUseCase;
import com.dnd.backend.application.incident.GetIncidentsByCursorUseCase;
import com.dnd.backend.application.incident.GetNearIncidentsUseCase;
import com.dnd.backend.application.incident.IncidentWithMediaAndDistanceDto;
import com.dnd.backend.application.incident.UpdateIncidentDescriptionUseCase;
import com.dnd.backend.application.incident.response.IncidentCursorResponse;
import com.dnd.backend.domain.incident.dto.UpdateIncidentCommand;
import com.dnd.backend.domain.incident.dto.WriteIncidentCommand;
import com.dnd.backend.domain.incident.entity.IncidentEntity;
import com.dnd.backend.domain.incident.service.IncidentReadService;
import com.dnd.backend.support.util.CursorRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incidents")
public class IncidentController {

	private final CreateIncidentUseCase createIncidentUseCase;
	private final UpdateIncidentDescriptionUseCase updateIncidentUseCase;
	private final GetIncidentsByCursorUseCase getIncidentsByCursorUseCase;
	private final GetNearIncidentsUseCase getNearIncidentsUsecase;
	private final IncidentReadService incidentReadService;
	private final DeleteIncidentUseCase deleteIncidentUseCase;
	private final CreateIncidentLikeUseCase createIncidentLikeUseCase;

	@PostMapping("/{incidentId}/like/v1")
	public void createIncidentLike(
		@PathVariable("incidentId") Long incidentId,
		@RequestParam("userId") Long userId
	) {
		createIncidentLikeUseCase.execute(incidentId, userId);
	}

	@GetMapping("/test/findAll")
	public List<IncidentEntity> createIncident() {
		return incidentReadService.findAll();
	}

	@PostMapping(consumes = {"multipart/form-data"})
	public void createIncident(
		@RequestPart("incidentData") WriteIncidentCommand command,
		@RequestPart(value = "files", required = false) List<MultipartFile> files) {

		//TODO : Null처리 DTO 캡슐화하기
		if (files == null) {
			files = Collections.emptyList();
		}
		createIncidentUseCase.execute(command, files);
	}

	@PatchMapping("/{incidentId}")
	public void updateIncident(
		@PathVariable("incidentId") Long incidentId,
		@Valid @RequestBody UpdateIncidentCommand command) {
		updateIncidentUseCase.execute(incidentId, command);
	}

	@DeleteMapping("/{incidentId}")
	public void deleteIncident(
		@PathVariable("incidentId") Long incidentId) {
		deleteIncidentUseCase.execute(incidentId);
	}

	@GetMapping("/writer/{writerId}")
	public IncidentCursorResponse getWriterIncidentsByCursor(
		@PathVariable("writerId") Long writerId,
		@ModelAttribute CursorRequest cursorRequest) {

		return getIncidentsByCursorUseCase.execute(writerId, cursorRequest);
	}

	@GetMapping("/nearby")
	public List<IncidentWithMediaAndDistanceDto> getIncidentsWithinDistance(
		@RequestParam double pointX,
		@RequestParam double pointY,
		@RequestParam(defaultValue = "5") double radiusInKm
	) {
		return getNearIncidentsUsecase.execute(pointX, pointY, radiusInKm);
	}
}
