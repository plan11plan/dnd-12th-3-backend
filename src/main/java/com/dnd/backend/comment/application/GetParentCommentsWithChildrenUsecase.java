package com.dnd.backend.comment.application;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.dnd.backend.comment.CommentEntity;
import com.dnd.backend.comment.CommentRepository;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetParentCommentsWithChildrenUsecase {

	private final CommentRepository commentRepository;

	public CursorResponse<CommentEntity> execute(Long incidentId, CursorRequest cursorRequest) {
		Pageable pageable = PageRequest.of(0, cursorRequest.size(), Sort.by(Sort.Direction.DESC, "id"));

		// 1단계: 부모 댓글 ID 조회
		List<Long> parentIds = commentRepository.findParentCommentIds(
			incidentId,
			cursorRequest.hasKey() ? cursorRequest.key() : null,
			pageable
		);

		if (parentIds.isEmpty()) {
			return new CursorResponse<>(cursorRequest.next(CursorRequest.NONE_KEY), List.of());
		}

		// 2단계: 부모 댓글 및 자식 댓글 조회
		List<CommentEntity> comments = commentRepository.findParentCommentsWithChildrenByIds(parentIds);

		long nextKey = parentIds.stream().min(Long::compare).orElse(CursorRequest.NONE_KEY);
		return new CursorResponse<>(cursorRequest.next(nextKey), comments);
	}

}
