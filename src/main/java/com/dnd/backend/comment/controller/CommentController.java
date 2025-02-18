package com.dnd.backend.comment.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.backend.comment.CommentEntity;
import com.dnd.backend.comment.application.CreateCommentReplyUsecase;
import com.dnd.backend.comment.application.CreateCommentUsecase;
import com.dnd.backend.comment.application.DeleteCommentUsecase;
import com.dnd.backend.comment.application.UpdateCommentUsecase;
import com.dnd.backend.user.security.CustomeUserDetails;
import com.dnd.backend.user.security.customAuthenticationPrincipal.AuthUser;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class CommentController {

	private final CreateCommentUsecase createCommentUsecase;
	private final CreateCommentReplyUsecase createCommentReplyUsecase;
	private final UpdateCommentUsecase updateCommentUsecase;
	private final DeleteCommentUsecase deleteCommentUsecase;

	@PostMapping("/{incidentId}/comments")
	public ResponseEntity<Void> createComment(
		@PathVariable Long incidentId,
		@RequestBody CreateCommentRequest request,
		@AuthUser CustomeUserDetails member) {
		createCommentUsecase.execute(
			incidentId,
			member.getId(),
			request.getContent()
		);
		return ResponseEntity.status(HttpStatus.CREATED).build();

	}

	@PostMapping("/{incidentId}/comments/{parentId}/replies")
	public ResponseEntity<Void> createCommentReply(
		@PathVariable Long incidentId,
		@PathVariable Long parentId,
		@RequestBody CreateCommentRequest request,
		@AuthUser CustomeUserDetails member) {
		createCommentReplyUsecase.execute(
			incidentId,
			member.getId(),
			request.getContent(),
			parentId);
		return ResponseEntity.ok().build();

	}

	@PutMapping("/{incidentId}/comments/{commentId}")
	public ResponseEntity<Void> updateComment(
		@PathVariable Long incidentId,
		@PathVariable Long commentId,
		@RequestBody UpdateCommentRequest request,
		@AuthUser CustomeUserDetails member) {
		updateCommentUsecase.execute(incidentId, commentId, member.getId(), request.getContent());
		return ResponseEntity.ok().build();

	}

	@DeleteMapping("/{incidentId}/comments/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long incidentId,
		@PathVariable Long commentId,
		@AuthUser CustomeUserDetails member) {
		deleteCommentUsecase.execute(incidentId, commentId, member.getId());
		return ResponseEntity.noContent().build();

	}

	@Data
	public static class CreateCommentRequest {
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
		private LocalDateTime createdAt;
		private List<CommentResponse> children;

		// includeChildren 플래그에 따라 자식 매핑 여부 결정
		public CommentResponse(CommentEntity entity, boolean includeChildren) {
			this.id = entity.getId();
			this.incidentId = entity.getIncidentId();
			this.writerId = entity.getWriterId();
			this.content = entity.getContent();
			this.parentId = entity.getParent() != null ? entity.getParent().getId() : null;
			this.createdAt = entity.getCreatedAt();
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
