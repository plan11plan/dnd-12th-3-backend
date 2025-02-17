package com.dnd.backend.incident.service;

import static org.springframework.data.domain.Sort.Direction.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dnd.backend.incident.dto.IncidentDistanceDto;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.entity.IncidentRepository;
import com.dnd.backend.incident.exception.IncidentNotFoundException;
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

	/**
	 * 화면 사각형 범위 내의 게시글 조회
	 * topLeftX: 화면 왼쪽 위의 경도 (최소 경도)
	 * bottomRightX: 화면 오른쪽 아래의 경도 (최대 경도)
	 * topLeftY: 화면 왼쪽 위의 위도 (최대 위도)
	 * bottomRightY: 화면 오른쪽 아래의 위도 (최소 위도)
	 */
	// 화면 사각형 영역 내의 게시글 조회
	public List<IncidentEntity> findIncidentsWithinScreen(
		double topRightX, double topRightY, // 오른쪽 위 좌표
		double bottomLeftX, double bottomLeftY // 왼쪽 아래 좌표
	) {
		List<IncidentEntity> allIncidents = incidentRepository.findAll()
			.orElseThrow(IncidentNotFoundException::new);
		return allIncidents.stream()
			// 경도: bottomLeftX(최소) ~ topRightX(최대)
			.filter(incident -> incident.getLongitude() >= bottomLeftX && incident.getLongitude() <= topRightX)
			// 위도: bottomLeftY(최소) ~ topRightY(최대)
			.filter(incident -> incident.getLatitude() >= bottomLeftY && incident.getLatitude() <= topRightY)
			.collect(Collectors.toList());
	}

	// Haversine 공식 (단위: km)
	public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371; // 지구의 반지름 (km)
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
			+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
			* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}

	public List<IncidentDistanceDto> findNearbyIncidents(double pointX, double pointY, double radiusKm) {
		List<IncidentEntity> allIncidents = incidentRepository.findAll()
			.orElseThrow(IncidentNotFoundException::new);

		return allIncidents.stream()
			.map(incident -> {
				double distance = calculateDistance(pointY, pointX,
					incident.getLongitude(), incident.getLatitude());
				return new IncidentDistanceDto(incident, distance);
			})
			.filter(dto -> dto.distance() <= radiusKm)
			.collect(Collectors.toList());
	}

	// // Haversine 공식을 사용하여 두 좌표 간의 거리 계산 (단위: km)
	// private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
	// 	final int R = 6371; // 지구의 반지름 (km)
	// 	double latDistance = Math.toRadians(lat2 - lat1); // 위도 차이
	// 	double lonDistance = Math.toRadians(lon2 - lon1); // 경도 차이
	// 	double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	// 		+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	// 		* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	// 	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	// 	return R * c; // 거리 (km)
	// }

	public IncidentEntity getIncident(Long incidentId) {
		return incidentRepository.findById(incidentId)
			.orElseThrow(IncidentNotFoundException::new);
	}
}

