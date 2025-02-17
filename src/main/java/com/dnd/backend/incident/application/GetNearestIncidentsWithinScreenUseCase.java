package com.dnd.backend.incident.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.dto.IncidentDistanceDto;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.service.IncidentReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetNearestIncidentsWithinScreenUseCase {

	private final IncidentReadService incidentReadService;
	private final IncidentWithMediaAssembler incidentWithMediaAssembler;

	public List<IncidentWithMediaAndDistanceDto> execute(
		double topLeftX,
		double topLeftY,
		double bottomRightX,
		double bottomRightY,
		double myX,
		double myY
	) {
		// 1. 화면 영역 내의 게시글 조회
		List<IncidentEntity> incidents = incidentReadService.findIncidentsWithinScreen(topLeftX, topLeftY, bottomRightX,
			bottomRightY);
		// 2. 내 위치 기준 각 게시글까지의 거리 계산
		List<IncidentDistanceDto> distanceDtos = incidents.stream()
			.map(incident -> {
				double distance = incidentReadService.calculateDistance(myY, myX, incident.getLatitude(),
					incident.getLongitude());
				return new IncidentDistanceDto(incident, distance);
			})
			// 3. 거리 오름차순 정렬
			.sorted(Comparator.comparingDouble(IncidentDistanceDto::distance))
			.collect(Collectors.toList());
		// 4. 작성자, 미디어 정보와 함께 DTO 변환하여 반환
		return incidentWithMediaAssembler.toIncidentWithMediaAndDistanceDtos(distanceDtos);
	}
}
