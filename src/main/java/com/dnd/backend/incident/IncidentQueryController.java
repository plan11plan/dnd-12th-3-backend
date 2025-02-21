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
import com.dnd.backend.incident.application.response.IncidentCursorResponse;
import com.dnd.backend.incident.dto.IncidentDto;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.service.IncidentReadService;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.user.security.CustomeUserDetails;
import com.dnd.backend.user.security.customAuthenticationPrincipal.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incidents")
public class IncidentQueryController {
	private final GetIncidentsByCursorUseCase getIncidentsByCursorUseCase;
	private final GetIncidentUsecase getIncidentUsecase;
	private final IncidentReadService incidentReadService;
	private final GetNewestIncidentsWithinScreenUseCase getNewestIncidentsWithinScreenUseCase;
	private final GetMostCommentedIncidentsWithinScreenUseCase getMostCommentedIncidentsWithinScreenUseCase;
	private final GetNearestIncidentsWithinScreenUseCase getNearestIncidentsWithinScreenUseCase;

	@GetMapping("/test/findAll")
	public List<IncidentEntity> createIncident() {
		return incidentReadService.findAll();
	}

	@GetMapping("/{incidentId}")
	public IncidentDto getIncident(
		@PathVariable("incidentId") Long incidentId,
		@RequestParam(required = false) Double myX,  // nullable
		@RequestParam(required = false) Double myY
	) {
		// myX, myY가 null일 수 있으니 예외처리/기본값 처리하거나, 필수 파라미터면 (required=true)로 선언
		return getIncidentUsecase.execute(incidentId, myX, myY);
	}

	@GetMapping("/my")
	public IncidentCursorResponse getWriterIncidentsByCursor(
		@AuthUser CustomeUserDetails customeUserDetails,
		@ModelAttribute CursorRequest cursorRequest) {
		Long writerId = customeUserDetails.getId();
		return getIncidentsByCursorUseCase.execute(writerId, cursorRequest);
	}

	@GetMapping("/newest")
	public IncidentCursorResponse getNewestIncidentsWithinScreen(
		@RequestParam double topRightX, @RequestParam double topRightY,
		@RequestParam double bottomLeftX, @RequestParam double bottomLeftY,
		@RequestParam double myX, @RequestParam double myY,
		@ModelAttribute CursorRequest cursorRequest
	) {
		return getNewestIncidentsWithinScreenUseCase.execute(topRightX, topRightY, bottomLeftX, bottomLeftY, myX, myY,
			cursorRequest);
	}

	@GetMapping("/most-commented")
	public IncidentCursorResponse getMostCommentedIncidentsWithinScreen(
		@RequestParam double topRightX, @RequestParam double topRightY,
		@RequestParam double bottomLeftX, @RequestParam double bottomLeftY,
		@RequestParam double myX, @RequestParam double myY,
		@ModelAttribute CursorRequest cursorRequest
	) {
		return getMostCommentedIncidentsWithinScreenUseCase.execute(topRightX, topRightY, bottomLeftX, bottomLeftY, myX,
			myY, cursorRequest);
	}

	@GetMapping("/nearest")
	public IncidentCursorResponse getNearestIncidentsWithinScreen(
		@RequestParam double topRightX, @RequestParam double topRightY,
		@RequestParam double bottomLeftX, @RequestParam double bottomLeftY,
		@RequestParam double myX, @RequestParam double myY,
		@ModelAttribute CursorRequest cursorRequest
	) {
		return getNearestIncidentsWithinScreenUseCase.execute(topRightX, topRightY, bottomLeftX, bottomLeftY, myX, myY,
			cursorRequest);
	}
}
