package com.dnd.backend.incident.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.dto.IncidentDistanceDto;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.service.IncidentReadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetNearIncidentsUseCase {

	private final IncidentReadService incidentReadService;
	private final IncidentWithMediaAssembler incidentWithMediaAssembler;

	public List<IncidentWithMediaAndDistanceDto> execute(
		double topLeftX,
		double topLeftY,
		double bottomRightX,
		double bottomRightY
	) {
		// 화면 중앙 좌표 계산
		double centerX = (topLeftX + bottomRightX) / 2;
		double centerY = (topLeftY + bottomRightY) / 2;

		// 1. 화면 범위 내의 게시글 조회
		List<IncidentEntity> incidents = incidentReadService.findIncidentsWithinScreen(topLeftX, topLeftY, bottomRightX,
			bottomRightY);

		// 2. 각 게시글과 화면 중앙과의 거리를 계산하여 IncidentDistanceDto로 매핑
		List<IncidentDistanceDto> incidentDistanceDtos = incidents.stream()
			.map(incident -> {
				double distance = incidentReadService.calculateDistance(centerY, centerX, incident.getLatitude(),
					incident.getLongitude());
				return new IncidentDistanceDto(incident, distance);
			})
			.collect(Collectors.toList());

		// 3. 미디어, 작성자 정보를 포함하여 최종 DTO 변환 (여기서는 거리 정보도 함께 포함)
		return incidentWithMediaAssembler.toIncidentWithMediaAndDistanceDtos(incidentDistanceDtos);
	}
}
