package com.dnd.backend.comment.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dnd.backend.comment.CommentEntity;
import com.dnd.backend.comment.CommentRepository;
import com.dnd.backend.comment.CommentResponse;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;
import com.dnd.backend.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetParentCommentsUsecase {

	private final CommentRepository commentRepository;
	private final MemberService memberService;

	public CursorResponse<CommentResponse> execute(Long incidentId, CursorRequest cursorRequest, Long currentUserId) {
		// (1) 페이지네이션 조회
		Pageable pageable = PageRequest.of(0, cursorRequest.size(), Sort.by(Sort.Direction.DESC, "id"));

		List<CommentEntity> comments;
		if (cursorRequest.hasKey()) {
			// 커서보다 작은 ID만 조회
			comments = commentRepository.findAllByIncidentIdAndParentIsNullAndIdLessThanOrderByIdDesc(
				incidentId,
				cursorRequest.key(),
				pageable
			).orElse(List.of());
		} else {
			// 처음 조회
			comments = commentRepository.findAllByIncidentIdAndParentIsNullOrderByIdDesc(incidentId, pageable)
				.orElse(List.of());
		}

		// (2) CommentEntity → CommentResponse 변환
		List<CommentResponse> commentResponses = comments.stream()
			.map(comment -> {
				// 작성자 이름
				String writerName = memberService.getMember(comment.getWriterId()).getName();

				// mine = (현재 로그인 사용자 ID == 댓글 작성자 ID)
				boolean mine = (currentUserId != null) && currentUserId.equals(comment.getWriterId());

				return new CommentResponse(
					comment.getId(),
					comment.getContent(),
					writerName,
					mine,
					comment.getCreatedAt(),
					comment.getParent() != null ? comment.getParent().getId() : null
				);
			})
			.collect(Collectors.toList());

		// (3) 다음 커서(nextKey) 계산
		long nextKey = comments.stream()
			.mapToLong(CommentEntity::getId)
			.min()
			.orElse(CursorRequest.NONE_KEY);

		// (4) CursorResponse<CommentResponse> 반환
		return new CursorResponse<>(cursorRequest.next(nextKey), commentResponses);
	}
}
