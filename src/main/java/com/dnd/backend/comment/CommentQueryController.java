package com.dnd.backend.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.backend.comment.application.GetCommentUsecase;
import com.dnd.backend.comment.application.GetCommentsUsecase;
import com.dnd.backend.comment.application.GetParentCommentsUsecase;
import com.dnd.backend.comment.application.GetParentCommentsWithChildrenUsecase;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;

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
	public ResponseEntity<CursorResponse<CommentController.CommentResponse>> getParentComments(
		@PathVariable Long incidentId,
		@ModelAttribute CursorRequest cursorRequest) {

		CursorResponse<?> response = getParentCommentsUsecase.execute(incidentId, cursorRequest);
		// 부모 댓글만 조회하므로 includeChildren=false
		List<CommentController.CommentResponse> dtos = ((List<?>)response.contents()).stream()
			.map(entity -> new CommentController.CommentResponse((CommentEntity)entity, false))
			.collect(Collectors.toList());

		return ResponseEntity.ok(new CursorResponse<>(response.nextCursorRequest(), dtos));
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
	public ResponseEntity<CommentController.CommentResponse> getComment(
		@PathVariable Long incidentId,
		@PathVariable Long commentId) {
		CommentEntity comment = getCommentUsecase.execute(commentId);
		return ResponseEntity.ok(new CommentController.CommentResponse(comment));
	}

	/**
	 * 게시글에 대한 상위 댓글 목록 조회
	 */
	@GetMapping("/{incidentId}/comments")
	public ResponseEntity<List<CommentController.CommentResponse>> getCommentsByIncident(
		@PathVariable Long incidentId) {
		List<CommentEntity> comments = getCommentsUsecase.execute(incidentId);
		List<CommentController.CommentResponse> responses = comments.stream()
			.map(CommentController.CommentResponse::new)
			.collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}

}
