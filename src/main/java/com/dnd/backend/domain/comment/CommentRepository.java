package com.dnd.backend.domain.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

	// 특정 게시글에 대한 상위(대댓글이 아닌) 댓글 목록 조회 (생성일 순 정렬)
	List<CommentEntity> findByIncidentIdAndParentIsNullOrderByCreatedAtAsc(Long incidentId);
}
