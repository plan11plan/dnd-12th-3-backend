package com.dnd.backend.comment.application;

import org.springframework.stereotype.Component;

import com.dnd.backend.comment.CommentEntity;
import com.dnd.backend.comment.CommentReadService;
import com.dnd.backend.comment.CommentResponse;
import com.dnd.backend.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCommentUsecase {

	private final CommentReadService commentReadService;
	private final MemberService memberService;

	public CommentResponse execute(Long commentId, Long currentUserId) {
		CommentEntity comment = commentReadService.getComment(commentId);

		String writerName = memberService.getMember(comment.getWriterId()).getName();
		boolean mine = (currentUserId != null) && currentUserId.equals(comment.getWriterId());

		return new CommentResponse(
			comment.getId(),
			comment.getContent(),
			writerName,
			mine,
			comment.getCreatedAt(),
			comment.getParent() != null ? comment.getParent().getId() : null
		);
	}
}
