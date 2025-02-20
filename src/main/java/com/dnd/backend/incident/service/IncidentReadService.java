package com.dnd.backend.incident.service;

import static org.springframework.data.domain.Sort.Direction.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dnd.backend.incident.dto.IncidentDistanceDto;
import com.dnd.backend.incident.dto.IncidentInfoDto;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.entity.IncidentRepository;
import com.dnd.backend.incident.exception.IncidentNotFoundException;
import com.dnd.backend.support.util.CursorRequest;
import com.dnd.backend.support.util.CursorResponse;
import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentReadService {
	private final IncidentRepository incidentRepository;
	private final MemberService memberService;
	private final IncidentLikeReadService incidentLikeReadService;

	public List<IncidentEntity> findAll() {
		return incidentRepository.findAll().orElseThrow(IncidentNotFoundException::new);
	}

	public CursorResponse<IncidentInfoDto> findIncidentsWithinScreenByCursor(
		double topRightX, double topRightY,
		double bottomLeftX, double bottomLeftY,
		CursorRequest cursorRequest
	) {
		MemberEntity currentMember = memberService.getCurrentMember();
		var pageable = PageRequest.of(
			0,
			cursorRequest.size(),
			Sort.by(DESC, "id")
		);

		List<IncidentEntity> incidents;
		if (cursorRequest.hasKey()) {
			incidents = incidentRepository.findAllWithinScreenAndIdLessThan(
					topRightX, topRightY, bottomLeftX, bottomLeftY,
					cursorRequest.key(), pageable)
				.orElseThrow(IncidentNotFoundException::new);
		} else {
			incidents = incidentRepository.findAllWithinScreen(
					topRightX, topRightY, bottomLeftX, bottomLeftY,
					pageable)
				.orElseThrow(IncidentNotFoundException::new);
		}

		var nextKey = incidents.stream()
			.mapToLong(IncidentEntity::getId)
			.min()
			.orElse(CursorRequest.NONE_KEY);

		List<IncidentInfoDto> incidentInfoDtos = incidents.stream()
			.map(incident -> new IncidentInfoDto(
				incident,
				incident.getWriterId().equals(currentMember.getId()),
				incidentLikeReadService.hasUserLiked(currentMember.getId(), incident.getId())
			))
			.collect(Collectors.toList());

		return new CursorResponse<>(cursorRequest.next(nextKey), incidentInfoDtos);
	}

	public CursorResponse<IncidentInfoDto> getIncidents(Long writerId, CursorRequest cursorRequest) {
		MemberEntity currentMember = memberService.getCurrentMember();
		var incidents = findAllBy(writerId, cursorRequest);
		var nextKey = incidents.stream()
			.mapToLong(IncidentEntity::getId)
			.min()
			.orElse(CursorRequest.NONE_KEY);

		List<IncidentInfoDto> incidentInfoDtos = incidents.stream()
			.map(incident -> new IncidentInfoDto(
				incident,
				incident.getWriterId().equals(currentMember.getId()),
				incidentLikeReadService.hasUserLiked(currentMember.getId(), incident.getId())
			))
			.collect(Collectors.toList());

		return new CursorResponse<>(cursorRequest.next(nextKey), incidentInfoDtos);
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
	public List<IncidentInfoDto> findIncidentsWithinScreen(
		double topRightX, double topRightY,
		double bottomLeftX, double bottomLeftY
	) {
		MemberEntity currentMember = memberService.getCurrentMember();
		List<IncidentEntity> allIncidents = incidentRepository.findAll()
			.orElseThrow(IncidentNotFoundException::new);
		return allIncidents.stream()
			.filter(incident -> incident.getLongitude() >= bottomLeftX && incident.getLongitude() <= topRightX)
			.filter(incident -> incident.getLatitude() >= bottomLeftY && incident.getLatitude() <= topRightY)
			.map(incident -> new IncidentInfoDto(
				incident,
				incident.getWriterId().equals(currentMember.getId()),
				incidentLikeReadService.hasUserLiked(currentMember.getId(), incident.getId())
			))
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
		MemberEntity currentMember = memberService.getCurrentMember();

		return allIncidents.stream()
			.map(incident -> {
				double distance = calculateDistance(pointY, pointX,
					incident.getLongitude(), incident.getLatitude());
				return new IncidentDistanceDto(
					incident,
					incident.getWriterId().equals(currentMember.getId()),
					incidentLikeReadService.hasUserLiked(currentMember.getId(), incident.getId()),
					distance);
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

	public IncidentInfoDto getIncident(Long incidentId) {
		MemberEntity currentMember = memberService.getCurrentMember();
		IncidentEntity incident = incidentRepository.findById(incidentId)
			.orElseThrow(IncidentNotFoundException::new);
		return new IncidentInfoDto(
			incident,
			incident.getWriterId().equals(currentMember.getId()),
			incidentLikeReadService.hasUserLiked(currentMember.getId(), incident.getId())
		);
	}
}

