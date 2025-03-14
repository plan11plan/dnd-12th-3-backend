package com.dnd.backend.incident.infra.repo.incident.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.incident.entity.IncidentEntity;

@Repository
public interface IncidentJpaRepository extends JpaRepository<IncidentEntity, Long> {

	List<IncidentEntity> findAllByWriterId(Long writerId, Pageable pageable);

	List<IncidentEntity> findAllByWriterIdAndIdLessThan(Long writerId, Long id, Pageable pageable);

	@Modifying
	@Query("DELETE FROM IncidentEntity i WHERE i.id = :id")
	int deleteByIdAndReturnCount(@Param("id") Long id);

	@Modifying
	@Query("UPDATE IncidentEntity i SET i.likeCount = :likeCount WHERE i.id = :incidentId")
	void updateLikeCount(@Param("incidentId") Long incidentId, @Param("likeCount") int likeCount);

	boolean existsByWriterIdAndId(Long writerId, Long incidentId);

	void deleteByWriterIdAndId(Long writerId, Long incidentId);

	@Modifying
	@Transactional
	@Query("UPDATE IncidentEntity i SET i.commentCount = :commentCount WHERE i.id = :incidentId")
	void updateCommentCount(@Param("incidentId") Long incidentId, @Param("commentCount") int commentCount);

	@Query("SELECT i FROM IncidentEntity i WHERE " +
		"i.longitude >= :bottomLeftX AND i.longitude <= :topRightX AND " +
		"i.latitude >= :bottomLeftY AND i.latitude <= :topRightY AND " +
		"i.id < :id ORDER BY i.id DESC")
	List<IncidentEntity> findAllWithinScreenAndIdLessThan(
		@Param("topRightX") double topRightX,
		@Param("topRightY") double topRightY,
		@Param("bottomLeftX") double bottomLeftX,
		@Param("bottomLeftY") double bottomLeftY,
		@Param("id") Long id,
		Pageable pageable);

	@Query("SELECT i FROM IncidentEntity i WHERE " +
		"i.longitude >= :bottomLeftX AND i.longitude <= :topRightX AND " +
		"i.latitude >= :bottomLeftY AND i.latitude <= :topRightY")
	List<IncidentEntity> findAllWithinScreen(
		@Param("topRightX") double topRightX,
		@Param("topRightY") double topRightY,
		@Param("bottomLeftX") double bottomLeftX,
		@Param("bottomLeftY") double bottomLeftY,
		Pageable pageable);
}

