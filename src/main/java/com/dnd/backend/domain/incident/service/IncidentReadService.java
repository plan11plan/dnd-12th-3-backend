package com.dnd.backend.domain.incident.service;

import static org.springframework.data.domain.Sort.Direction.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dnd.backend.domain.incident.dto.IncidentDistanceDto;
import com.dnd.backend.domain.incident.entity.IncidentEntity;
import com.dnd.backend.domain.incident.exception.IncidentNotFoundException;
import com.dnd.backend.domain.incident.repository.IncidentRepository;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentReadService {
	private final IncidentRepository incidentRepository;

	public List<IncidentEntity> findAll() {
		return incidentRepository.findAll().orElseThrow(IncidentNotFoundException::new);
	}

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
			return incidentRepository.findAllByWriterIdAndIdLessThan(
					writerId,
					cursorRequest.key(),
					pageable)
				.orElseThrow(IncidentNotFoundException::new);
		}
		return incidentRepository.findAllByWriterId(
				writerId,
				pageable)
			.orElseThrow(IncidentNotFoundException::new);
	}

	public List<IncidentDistanceDto> findNearbyIncidents(double pointX, double pointY, double radiusKm) {
		List<IncidentEntity> allIncidents = incidentRepository.findAll()
			.orElseThrow(IncidentNotFoundException::new);

		return allIncidents.stream()
			.map(incident -> {
				double distance = calculateDistance(pointY, pointX,
					incident.getPointY(), incident.getPointX());
				return new IncidentDistanceDto(incident, distance);
			})
			.filter(dto -> dto.distance() <= radiusKm)
			.collect(Collectors.toList());
	}

	// Haversine 공식을 사용하여 두 좌표 간의 거리 계산 (단위: km)
	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371; // 지구의 반지름 (km)
		double latDistance = Math.toRadians(lat2 - lat1); // 위도 차이
		double lonDistance = Math.toRadians(lon2 - lon1); // 경도 차이
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
			+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
			* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c; // 거리 (km)
	}

	public IncidentEntity getIncident(Long incidentId) {
		return incidentRepository.findById(incidentId)
			.orElseThrow(IncidentNotFoundException::new);
	}
}

