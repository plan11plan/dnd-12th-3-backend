package com.dnd.backend.comment.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dnd.backend.comment.CommentEntity;
import com.dnd.backend.comment.CommentReadService;
import com.dnd.backend.comment.CommentResponse;
import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCommentsUsecase {

	private final CommentReadService commentReadService;
	private final MemberService memberService;

	/**
	 * @param incidentId    게시글 ID
	 * @param currentUserId 로그인한 사용자 ID (없으면 null)
	 */
	public List<CommentResponse> execute(Long incidentId, Long currentUserId) {
		// 1) 댓글 엔티티 리스트 가져오기
		List<CommentEntity> commentEntities = commentReadService.getCommentsByIncident(incidentId);

		// 2) 엔티티 → DTO 변환
		return commentEntities.stream()
			.map(comment -> {
				// 작성자 MemberEntity 조회 → 작성자 이름
				MemberEntity writer = memberService.getMember(comment.getWriterId());
				String writerName = writer.getName();

				// "mine" 여부: 현재 사용자 ID와 댓글의 writerId가 일치하면 true
				boolean isMine = (currentUserId != null) && currentUserId.equals(comment.getWriterId());

				return new CommentResponse(
					comment.getId(),                  // commentId
					comment.getContent(),             // content
					writerName,                       // writerName
					isMine,                           // mine
					comment.getCreatedAt(),           // createdAt
					comment.getParent() == null ? null : comment.getParent().getId()
				);
			})
			.collect(Collectors.toList());
	}
}
