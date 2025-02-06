package com.dnd.backend.domain.incidnet.service;

import static org.springframework.data.domain.Sort.Direction.*;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.incidnet.entity.IncidentEntity;
import com.dnd.backend.domain.incidnet.repository.JpaIncidentRepository;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentReadService {
	private final JpaIncidentRepository incidentQueryRepository;

	public CursorResponse<IncidentEntity> getIncidents(Long writerId, CursorRequest cursorRequest) {
		var incidents = findAllBy(writerId, cursorRequest);
		var nextKey = incidents.stream()
			.mapToLong(IncidentEntity::getId)
			.min()
			.orElse(CursorRequest.NONE_KEY);

		return new CursorResponse<>(cursorRequest.next(nextKey), incidents);
	}

	private List<IncidentEntity> findAllBy(Long writerId, CursorRequest cursorRequest) {
		var pageable = PageRequest.of(
			0,
			cursorRequest.size(),
			Sort.by(DESC, "id")
		);
		if (cursorRequest.hasKey()) {
			return incidentQueryRepository.findAllByWriterIdAndIdLessThan(
				writerId,
				cursorRequest.key(),
				pageable);
		}
		return incidentQueryRepository.findAllByWriterId(
			writerId,
			pageable);
	}
}

