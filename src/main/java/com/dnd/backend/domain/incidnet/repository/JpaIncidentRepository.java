package com.dnd.backend.domain.incidnet.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dnd.backend.domain.incidnet.entity.IncidentEntity;

@Repository
public interface JpaIncidentRepository extends JpaRepository<IncidentEntity, Long> {
	List<IncidentEntity> findAllByWriterId(Long writerId, Pageable pageable);

	List<IncidentEntity> findAllByWriterIdAndIdLessThan(Long writerId, Long id, Pageable pageable);
}
