package com.dnd.backend.incident.application;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.application.response.IncidentWithMediaDto;
import com.dnd.backend.incident.dto.IncidentDistanceDto;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.mediaFile.dto.MediaFileInfo;
import com.dnd.backend.mediaFile.service.MediaFileReadService;
import com.dnd.backend.support.util.DistanceFormatter;
import com.dnd.backend.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IncidentWithMediaAssembler {

	private final MediaFileReadService mediaFileReadService;
	private final MemberService memberService;  // 작성자 정보 조회를 위한 서비스

	public List<IncidentWithMediaDto> toIncidentWithMediaDtos(List<IncidentEntity> incidents) {
		if (incidents.isEmpty()) {
			return Collections.emptyList();
		}
		var incidentIds = incidents.stream()
			.map(IncidentEntity::getId)
			.collect(Collectors.toList());

		// 유저 id 모음
		var writerIds = incidents.stream()
			.map(IncidentEntity::getWriterId)
			.distinct()
			.collect(Collectors.toList());
		var writerMap = memberService.getMembersByIds(writerIds);

		var allMediaFiles = mediaFileReadService.getMediaFilesByIncidentIds(incidentIds);
		var mediaFilesGrouped = allMediaFiles.stream()
			.collect(Collectors.groupingBy(MediaFileInfo::incidentId));

		return incidents.stream()
			.map(incident -> {
				var writer = writerMap.get(incident.getWriterId());
				// 작성자 정보가 없을 경우 기본값 설정
				var writerName = writer != null ? writer.getName() : "Unknown";
				var writerInfo = new IncidentWriterInfo(writerName);
				var mediaFiles = mediaFilesGrouped.getOrDefault(incident.getId(), Collections.emptyList());
				return new IncidentWithMediaDto(writerInfo, incident, mediaFiles);
			})
			.collect(Collectors.toList());
	}

	public List<IncidentWithMediaAndDistanceDto> toIncidentWithMediaAndDistanceDtos(
		List<IncidentDistanceDto> incidentDistances) {

		if (incidentDistances.isEmpty()) {
			return Collections.emptyList();
		}

		var incidentIds = incidentDistances.stream()
			.map(dto -> dto.incident().getId())
			.toList();

		var allMediaFiles = mediaFileReadService.getMediaFilesByIncidentIds(incidentIds);
		var mediaFilesGrouped = allMediaFiles.stream()
			.collect(Collectors.groupingBy(MediaFileInfo::incidentId));

		var writer = new IncidentWriterInfo("mockNickname");

		return incidentDistances.stream()
			.map(dto -> {
				var incident = dto.incident();
				var distance = dto.distance();
				var mediaFiles = mediaFilesGrouped.getOrDefault(incident.getId(), Collections.emptyList());
				return new IncidentWithMediaAndDistanceDto(writer, incident, DistanceFormatter.formatDistance(distance),
					mediaFiles);
			})
			.collect(Collectors.toList());
	}

}
