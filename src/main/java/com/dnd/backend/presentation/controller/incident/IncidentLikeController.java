package com.dnd.backend.presentation.controller.incident;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.backend.domain.incident.service.IncidentLikeReadService;
import com.dnd.backend.domain.incident.service.IncidentLikeWriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/incidents/{incidentId}/likes")
@RequiredArgsConstructor
public class IncidentLikeController {

	private final IncidentLikeWriteService incidentLikeWriteService;
	private final IncidentLikeReadService incidentLikeReadService;

	/**
	 * 사건사고 좋아요 추가
	 */
	@PostMapping
	public ResponseEntity<String> likeIncident(
		@PathVariable Long incidentId,
		@RequestParam Long writerId // 좋아요를 누르는 사용자 ID
	) {
		incidentLikeWriteService.like(writerId, incidentId);
		return ResponseEntity.ok("👍 좋아요가 추가되었습니다.");
	}

	/**
	 * 사건사고 좋아요 개수 조회
	 */
	@GetMapping("/count")
	public ResponseEntity<Integer> getLikeCount(@PathVariable Long incidentId) {
		int likeCount = incidentLikeReadService.getLikeCount(incidentId);
		return ResponseEntity.ok(likeCount);
	}
}
