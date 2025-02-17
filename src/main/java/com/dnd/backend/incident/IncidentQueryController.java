package com.dnd.backend.incident;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.backend.incident.application.GetIncidentUsecase;
import com.dnd.backend.incident.application.GetIncidentsByCursorUseCase;
import com.dnd.backend.incident.application.GetMostCommentedIncidentsWithinScreenUseCase;
import com.dnd.backend.incident.application.GetNearestIncidentsWithinScreenUseCase;
import com.dnd.backend.incident.application.GetNewestIncidentsWithinScreenUseCase;
import com.dnd.backend.incident.application.IncidentWithMediaAndDistanceDto;
import com.dnd.backend.incident.application.response.IncidentCursorResponse;
import com.dnd.backend.incident.dto.IncidentDto;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.service.IncidentReadService;
import com.dnd.backend.support.util.CursorRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incidents")
public class IncidentQueryController {
	private final GetIncidentsByCursorUseCase getIncidentsByCursorUseCase;
	private final GetIncidentUsecase getIncidentUsecase;
	private final IncidentReadService incidentReadService;
	private final GetNewestIncidentsWithinScreenUseCase getNewestIncidentsUseCase;
	private final GetMostCommentedIncidentsWithinScreenUseCase getMostCommentedIncidentsUseCase;
	private final GetNearestIncidentsWithinScreenUseCase getNearestIncidentsUseCase;

	@GetMapping("/test/findAll")
	public List<IncidentEntity> createIncident() {
		return incidentReadService.findAll();
	}

	@GetMapping("/{incidentId}")
	public IncidentDto getIncident(
		@PathVariable("incidentId") Long incidentId) {
		return getIncidentUsecase.execute(incidentId);
	}

	@GetMapping("/writer/{writerId}")
	public IncidentCursorResponse getWriterIncidentsByCursor(
		@PathVariable("writerId") Long writerId,
		@ModelAttribute CursorRequest cursorRequest) {

		return getIncidentsByCursorUseCase.execute(writerId, cursorRequest);
	}

	// 최신순: 생성일 기준 내림차순 정렬 + 내 위치와의 거리 포함
	@GetMapping("/newest")
	public List<IncidentWithMediaAndDistanceDto> getNewestIncidentsWithinScreen(
		@RequestParam double topLeftX,
		@RequestParam double topLeftY,
		@RequestParam double bottomRightX,
		@RequestParam double bottomRightY,
		@RequestParam double myX,
		@RequestParam double myY
	) {
		return getNewestIncidentsUseCase.execute(topLeftX, topLeftY, bottomRightX, bottomRightY, myX, myY);
	}

	// 댓글 많은순: 댓글 수 기준 내림차순 정렬 + 내 위치와의 거리 포함
	@GetMapping("/most-commented")
	public List<IncidentWithMediaAndDistanceDto> getMostCommentedIncidentsWithinScreen(
		@RequestParam double topLeftX,
		@RequestParam double topLeftY,
		@RequestParam double bottomRightX,
		@RequestParam double bottomRightY,
		@RequestParam double myX,
		@RequestParam double myY
	) {
		return getMostCommentedIncidentsUseCase.execute(topLeftX, topLeftY, bottomRightX, bottomRightY, myX, myY);
	}

	// 가까운순: 내 위치 기준 거리 오름차순 정렬 (내 위치로부터의 거리 포함)
	@GetMapping("/nearest")
	public List<IncidentWithMediaAndDistanceDto> getNearestIncidentsWithinScreen(
		@RequestParam double topLeftX,
		@RequestParam double topLeftY,
		@RequestParam double bottomRightX,
		@RequestParam double bottomRightY,
		@RequestParam double myX,
		@RequestParam double myY
	) {
		return getNearestIncidentsUseCase.execute(topLeftX, topLeftY, bottomRightX, bottomRightY, myX, myY);
	}
}
