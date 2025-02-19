package com.dnd.backend.comment.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.backend.comment.CommentEntity;
import com.dnd.backend.comment.CommentResponse;
import com.dnd.backend.comment.application.GetCommentUsecase;
import com.dnd.backend.comment.application.GetCommentsUsecase;
import com.dnd.backend.comment.application.GetParentCommentsUsecase;
import com.dnd.backend.comment.application.GetParentCommentsWithChildrenUsecase;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;
import com.dnd.backend.user.security.CustomeUserDetails;
import com.dnd.backend.user.security.customAuthenticationPrincipal.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class CommentQueryController {

	private final GetCommentsUsecase getCommentsUsecase;
	private final GetCommentUsecase getCommentUsecase;

	private final GetParentCommentsUsecase getParentCommentsUsecase;
	private final GetParentCommentsWithChildrenUsecase getParentCommentsWithChildrenUsecase;

	/**
	 * 부모 댓글만 커서 조회 (대댓글 제외)
	 */
	@GetMapping("/{incidentId}/comments/cursor")
	public CursorResponse<CommentResponse> getParentComments(
		@PathVariable Long incidentId,
		@ModelAttribute CursorRequest cursorRequest,
		@AuthUser CustomeUserDetails member // 로그인 사용자 정보
	) {
		// member가 null일 수 있으므로, id 안전처리
		Long currentUserId = (member != null) ? member.getId() : null;

		// Usecase 호출 시 userId 넘김
		CursorResponse<CommentResponse> response = getParentCommentsUsecase.execute(incidentId, cursorRequest,
			currentUserId);
		return response;
	}

	/**
	 * 부모 댓글과 해당 대댓글들을 포함하여 커서 조회
	 */
	@GetMapping("/{incidentId}/comments/cursor/full")
	public ResponseEntity<CursorResponse<CommentController.CommentResponse>> getParentCommentsWithChildren(
		@PathVariable Long incidentId,
		@ModelAttribute CursorRequest cursorRequest) {

		CursorResponse<?> response = getParentCommentsWithChildrenUsecase.execute(incidentId, cursorRequest);
		// 부모 댓글과 대댓글 모두 포함하므로 includeChildren=true
		List<CommentController.CommentResponse> dtos = ((List<?>)response.contents()).stream()
			.map(entity -> new CommentController.CommentResponse((CommentEntity)entity, true))
			.collect(Collectors.toList());

		return ResponseEntity.ok(new CursorResponse<>(response.nextCursorRequest(), dtos));
	}

	/**
	 * 단건 댓글 조회
	 */
	@GetMapping("/{incidentId}/comments/{commentId}")
	public ResponseEntity<CommentResponse> getComment(
		@PathVariable Long incidentId,
		@PathVariable Long commentId,
		@AuthUser CustomeUserDetails member
	) {
		Long currentUserId = (member != null) ? member.getId() : null;
		// usecase가 CommentResponse를 반환하도록 변경
		CommentResponse commentResponse = getCommentUsecase.execute(commentId, currentUserId);
		return ResponseEntity.ok(commentResponse);
	}

	@GetMapping("/{incidentId}/comments")
	public ResponseEntity<List<CommentResponse>> getCommentsByIncident(
		@PathVariable Long incidentId,
		@AuthUser CustomeUserDetails member // 로그인 사용자 정보 주입
	) {
		// member가 null일 수도 있으니 체크
		Long currentUserId = (member != null) ? member.getId() : null;

		// Usecase 호출 시 userId 넘김
		var comments = getCommentsUsecase.execute(incidentId, currentUserId);

		return ResponseEntity.ok(comments);
	}

}
