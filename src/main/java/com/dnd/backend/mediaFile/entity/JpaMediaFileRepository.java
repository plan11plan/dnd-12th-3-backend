package com.dnd.backend.mediaFile.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaMediaFileRepository extends JpaRepository<MediaFileEntity, Long> {
	int countByIncidentId(Long incidentId);

	@Query("SELECT m FROM MediaFileEntity m WHERE m.incidentId IN :incidentIds")
	List<MediaFileEntity> findMediaFilesByIncidentIds(@Param("incidentIds") List<Long> incidentIds);

	@Query("SELECT m FROM MediaFileEntity m WHERE m.incidentId IN :incidentIds")
	List<MediaFileEntity> findByIncidentIdIn(@Param("incidentIds") List<Long> incidentIds);
}
