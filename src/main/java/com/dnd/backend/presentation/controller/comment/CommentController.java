package com.dnd.backend.presentation.controller.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.backend.application.comment.CreateCommentReplyUsecase;
import com.dnd.backend.application.comment.CreateCommentUsecase;
import com.dnd.backend.application.comment.DeleteCommentUsecase;
import com.dnd.backend.application.comment.GetCommentUsecase;
import com.dnd.backend.application.comment.GetCommentsUsecase;
import com.dnd.backend.application.comment.UpdateCommentUsecase;
import com.dnd.backend.domain.comment.CommentEntity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/incidents/{incidentId}/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CreateCommentUsecase createCommentUsecase;
	private final CreateCommentReplyUsecase createCommentReplyUsecase;
	private final UpdateCommentUsecase updateCommentUsecase;
	private final DeleteCommentUsecase deleteCommentUsecase;
	private final GetCommentsUsecase getCommentsUsecase;
	private final GetCommentUsecase getCommentUsecase;

	@PostMapping
	public void createComment(
		@PathVariable Long incidentId,
		@RequestBody CreateCommentRequest request) {
		createCommentUsecase.execute(incidentId, request.getWriterId(), request.getContent());
	}

	@PostMapping("/{parentId}/replies")
	public void createCommentReply(
		@PathVariable Long incidentId,
		@PathVariable Long parentId,
		@RequestBody CreateCommentRequest request) {
		createCommentReplyUsecase.execute(incidentId, request.getWriterId(), request.getContent(), parentId);
	}

	@PutMapping("/{commentId}")
	public void updateComment(
		@PathVariable Long incidentId,
		@PathVariable Long commentId,
		@RequestBody UpdateCommentRequest request) {
		updateCommentUsecase.execute(incidentId, commentId, request.getWriterId(), request.getContent());
	}

	@DeleteMapping("/{commentId}")
	public void deleteComment(@PathVariable Long incidentId, @PathVariable Long commentId) {
		deleteCommentUsecase.execute(incidentId, commentId);
	}

	/**
	 * 단건 댓글 조회
	 */
	@GetMapping("/{commentId}")
	public ResponseEntity<CommentResponse> getComment(
		@PathVariable Long incidentId,
		@PathVariable Long commentId) {
		CommentEntity comment = getCommentUsecase.execute(commentId);
		return ResponseEntity.ok(new CommentResponse(comment));
	}

	/**
	 * 게시글에 대한 상위 댓글 목록 조회
	 */
	@GetMapping
	public ResponseEntity<List<CommentResponse>> getCommentsByIncident(@PathVariable Long incidentId) {
		List<CommentEntity> comments = getCommentsUsecase.execute(incidentId);
		List<CommentResponse> responses = comments.stream()
			.map(CommentResponse::new)
			.collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}

	@Data
	public static class CreateCommentRequest {
		private Long writerId;
		private String content;
	}

	@Data
	public static class UpdateCommentRequest {
		private Long writerId;
		private String content;
	}

	@Data
	public static class CommentResponse {
		private Long id;
		private Long incidentId;
		private Long writerId;
		private String content;
		private Long parentId;
		private List<CommentResponse> children;

		public CommentResponse(CommentEntity entity) {
			this.id = entity.getId();
			this.incidentId = entity.getIncidentId();
			this.writerId = entity.getWriterId();
			this.content = entity.getContent();
			this.parentId = entity.getParent() != null ? entity.getParent().getId() : null;
			// 자식 댓글은 필요한 경우 재귀적으로 매핑 (여기서는 간단히 처리)
			this.children = entity.getChildren().stream()
				.map(CommentResponse::new)
				.collect(Collectors.toList());
		}
	}
}
