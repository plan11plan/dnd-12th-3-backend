package com.dnd.backend.comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

	List<CommentEntity> findByIncidentIdAndParentIsNullOrderByCreatedAtAsc(Long incidentId);

	Optional<List<CommentEntity>> findAllByIncidentIdAndParentIsNullAndIdLessThanOrderByIdDesc(
		Long incidentId, Long id, Pageable pageable);

	Optional<List<CommentEntity>> findAllByIncidentIdAndParentIsNullOrderByIdDesc(
		Long incidentId, Pageable pageable);

	@Query("select c.id from CommentEntity c " +
		"where c.incidentId = :incidentId and c.parent is null " +
		"and (:cursor is null or c.id < :cursor) " +
		"order by c.id desc")
	List<Long> findParentCommentIds(
		@Param("incidentId") Long incidentId,
		@Param("cursor") Long cursor,
		Pageable pageable);

	@Query("select distinct c from CommentEntity c " +
		"left join fetch c.children " +
		"where c.id in :parentIds")
	List<CommentEntity> findParentCommentsWithChildrenByIds(@Param("parentIds") List<Long> parentIds);
}
