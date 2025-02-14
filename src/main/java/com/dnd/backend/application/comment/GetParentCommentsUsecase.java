package com.dnd.backend.application.comment;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.comment.CommentEntity;
import com.dnd.backend.domain.comment.CommentRepository;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetParentCommentsUsecase {

	private final CommentRepository commentRepository;

	public CursorResponse<CommentEntity> execute(Long incidentId, CursorRequest cursorRequest) {
		Pageable pageable = PageRequest.of(0, cursorRequest.size(), Sort.by(Sort.Direction.DESC, "id"));
		List<CommentEntity> comments;

		if (cursorRequest.hasKey()) {
			comments = commentRepository.findAllByIncidentIdAndParentIsNullAndIdLessThanOrderByIdDesc(
				incidentId,
				cursorRequest.key(),
				pageable
			).orElse(List.of());
		} else {
			comments = commentRepository
				.findAllByIncidentIdAndParentIsNullOrderByIdDesc(incidentId, pageable)
				.orElse(List.of());
		}

		long nextKey = comments.stream()
			.mapToLong(CommentEntity::getId)
			.min()
			.orElse(CursorRequest.NONE_KEY);

		return new CursorResponse<>(cursorRequest.next(nextKey), comments);
	}
}
