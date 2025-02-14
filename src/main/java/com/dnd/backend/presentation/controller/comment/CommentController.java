package com.dnd.backend.presentation.controller.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.dnd.backend.application.comment.GetParentCommentsUsecase;
import com.dnd.backend.application.comment.GetParentCommentsWithChildrenUsecase;
import com.dnd.backend.application.comment.UpdateCommentUsecase;
import com.dnd.backend.domain.comment.CommentEntity;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;

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

	private final GetParentCommentsUsecase getParentCommentsUsecase;
	private final GetParentCommentsWithChildrenUsecase getParentCommentsWithChildrenUsecase;

	/**
	 * 부모 댓글만 커서 조회 (대댓글 제외)
	 */
	@GetMapping("/cursor")
	public ResponseEntity<CursorResponse<CommentResponse>> getParentComments(
		@PathVariable Long incidentId,
		@ModelAttribute CursorRequest cursorRequest) {

		CursorResponse<?> response = getParentCommentsUsecase.execute(incidentId, cursorRequest);
		// 부모 댓글만 조회하므로 includeChildren=false
		List<CommentResponse> dtos = ((List<?>)response.contents()).stream()
			.map(entity -> new CommentResponse((com.dnd.backend.domain.comment.CommentEntity)entity, false))
			.collect(Collectors.toList());

		return ResponseEntity.ok(new CursorResponse<>(response.nextCursorRequest(), dtos));
	}

	/**
	 * 부모 댓글과 해당 대댓글들을 포함하여 커서 조회
	 */
	@GetMapping("/cursor/full")
	public ResponseEntity<CursorResponse<CommentResponse>> getParentCommentsWithChildren(
		@PathVariable Long incidentId,
		@ModelAttribute CursorRequest cursorRequest) {

		CursorResponse<?> response = getParentCommentsWithChildrenUsecase.execute(incidentId, cursorRequest);
		// 부모 댓글과 대댓글 모두 포함하므로 includeChildren=true
		List<CommentResponse> dtos = ((List<?>)response.contents()).stream()
			.map(entity -> new CommentResponse((com.dnd.backend.domain.comment.CommentEntity)entity, true))
			.collect(Collectors.toList());

		return ResponseEntity.ok(new CursorResponse<>(response.nextCursorRequest(), dtos));
	}

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

		// includeChildren 플래그에 따라 자식 매핑 여부 결정
		public CommentResponse(CommentEntity entity, boolean includeChildren) {
			this.id = entity.getId();
			this.incidentId = entity.getIncidentId();
			this.writerId = entity.getWriterId();
			this.content = entity.getContent();
			this.parentId = entity.getParent() != null ? entity.getParent().getId() : null;
			if (includeChildren && entity.getChildren() != null && !entity.getChildren().isEmpty()) {
				this.children = entity.getChildren().stream()
					.map(child -> new CommentResponse(child, true))
					.collect(Collectors.toList());
			} else {
				this.children = null; // 또는 Collections.emptyList();
			}
		}

		// 기존 생성자를 기본적으로 includeChildren=true로 호출
		public CommentResponse(CommentEntity entity) {
			this(entity, true);
		}
	}
}
