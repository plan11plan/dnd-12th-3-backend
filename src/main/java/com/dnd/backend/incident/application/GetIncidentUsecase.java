package com.dnd.backend.incident.application;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.dto.IncidentDto;
import com.dnd.backend.incident.dto.IncidentInfoDto;
import com.dnd.backend.incident.service.IncidentReadService;
import com.dnd.backend.mediaFile.dto.MediaFileInfo;
import com.dnd.backend.mediaFile.service.MediaFileReadService;
import com.dnd.backend.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetIncidentUsecase {

	private final IncidentReadService incidentReadService;
	private final MemberService memberService;
	private final MediaFileReadService mediaFileReadService;

	public IncidentDto execute(Long incidentId, Double myX, Double myY) {
		// 1) 기본 IncidentInfoDto 조회
		IncidentInfoDto incident = incidentReadService.getIncident(incidentId);

		// 2) 작성자 정보, 미디어 파일 조회
		var writerId = incident.incidentEntity().getWriterId();
		var writerName = memberService.getMember(writerId).getName();

		List<MediaFileInfo> mediaFilesByIncidentIds = mediaFileReadService.getMediaFilesByIncidentIds(
			List.of(incidentId));

		// 3) 거리 계산 (myX,myY가 null이 아니면)
		double distance = 0.0;
		if (myX != null && myY != null) {
			// incident의 lat/lon
			double lat = incident.incidentEntity().getLatitude();
			double lon = incident.incidentEntity().getLongitude();
			distance = incidentReadService.calculateDistance(myY, myX, lat, lon);
		}

		// 4) IncidentDto 생성 + distance 추가
		// 여기선 IncidentDto 를 새롭게 확장하거나,
		// Record 필드에 distance 추가하는 방식
		return IncidentDto.from(
			incident.incidentEntity(),
			incident.liked(),
			incident.editable(),
			writerName,
			mediaFilesByIncidentIds,
			distance  // 새 파라미터
		);
	}
}

