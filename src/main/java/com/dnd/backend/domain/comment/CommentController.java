package com.dnd.backend.domain.comment;

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

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/incidents/{incidentId}/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentUseCase commentUseCase;

	/**
	 * 게시글 댓글 생성
	 */
	@PostMapping
	public ResponseEntity<CommentResponse> createComment(
		@PathVariable Long incidentId,
		@RequestBody CreateCommentRequest request) {
		CommentEntity comment = commentUseCase.createComment(incidentId, request.getWriterId(), request.getContent());
		return ResponseEntity.ok(new CommentResponse(comment));
	}

	/**
	 * 게시글의 대댓글(답글) 생성
	 */
	@PostMapping("/{parentId}/replies")
	public ResponseEntity<CommentResponse> createReply(
		@PathVariable Long incidentId,
		@PathVariable Long parentId,
		@RequestBody CreateCommentRequest request) {
		CommentEntity reply = commentUseCase.createReply(incidentId, request.getWriterId(), request.getContent(),
			parentId);
		return ResponseEntity.ok(new CommentResponse(reply));
	}

	/**
	 * 단건 댓글 조회
	 */
	@GetMapping("/{commentId}")
	public ResponseEntity<CommentResponse> getComment(
		@PathVariable Long incidentId,
		@PathVariable Long commentId) {
		CommentEntity comment = commentUseCase.getComment(commentId);
		return ResponseEntity.ok(new CommentResponse(comment));
	}

	/**
	 * 게시글에 대한 상위 댓글 목록 조회
	 */
	@GetMapping
	public ResponseEntity<List<CommentResponse>> getCommentsByIncident(@PathVariable Long incidentId) {
		List<CommentEntity> comments = commentUseCase.getCommentsByIncident(incidentId);
		List<CommentResponse> responses = comments.stream()
			.map(CommentResponse::new)
			.collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}

	/**
	 * 댓글 수정
	 */
	@PutMapping("/{commentId}")
	public ResponseEntity<CommentResponse> updateComment(
		@PathVariable Long incidentId,
		@PathVariable Long commentId,
		@RequestBody UpdateCommentRequest request) {
		CommentEntity updated = commentUseCase.updateComment(commentId, request.getContent());
		return ResponseEntity.ok(new CommentResponse(updated));
	}

	/**
	 * 댓글 삭제
	 */
	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(
		@PathVariable Long incidentId,
		@PathVariable Long commentId) {
		commentUseCase.deleteComment(commentId);
		return ResponseEntity.noContent().build();
	}

	@Data
	public static class CreateCommentRequest {
		private Long writerId;
		private String content;
	}

	@Data
	public static class UpdateCommentRequest {
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
