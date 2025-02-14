package com.dnd.backend.presentation.controller.incident;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.backend.application.incident.ToggleLikeIncidentUsecase;
import com.dnd.backend.domain.incident.service.IncidentLikeReadService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/incidents")

@RestController
@RequiredArgsConstructor
public class IncidentLikeController {

	private final IncidentLikeReadService incidentLikeReadService;
	private final ToggleLikeIncidentUsecase toggleLikeIncidentUsecase;

	@PostMapping("/{incidentId}/likes/toggle")
	public String toggleLike(@PathVariable Long incidentId, @RequestParam Long userId) {
		return toggleLikeIncidentUsecase.execute(userId, incidentId);
	}

	/**
	 * 현재 좋아요 수 조회 API
	 * 요청 예: GET /api/incidents/1/likes/count
	 */
	@GetMapping("/{incidentId}/likes/count")
	public ResponseEntity<Integer> getLikeCount(@PathVariable Long incidentId) {
		int likeCount = incidentLikeReadService.getLikeCount(incidentId);
		return ResponseEntity.ok(likeCount);
	}
}
