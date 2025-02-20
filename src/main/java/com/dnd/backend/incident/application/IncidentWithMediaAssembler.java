package com.dnd.backend.incident.application;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.application.response.IncidentWithMediaDto;
import com.dnd.backend.incident.dto.IncidentDistanceDto;
import com.dnd.backend.incident.dto.IncidentInfoDto;
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

	public List<IncidentWithMediaDto> toIncidentWithMediaDtos(List<IncidentInfoDto> incidentInfoDtos) {
		if (incidentInfoDtos.isEmpty()) {
			return Collections.emptyList();
		}

		// Extract incident IDs and writer IDs
		var incidentIds = incidentInfoDtos.stream()
			.map(dto -> dto.incident().getId())
			.collect(Collectors.toList());

		var writerIds = incidentInfoDtos.stream()
			.map(dto -> dto.incident().getWriterId())
			.distinct()
			.collect(Collectors.toList());
		var writerMap = memberService.getMembersByIds(writerIds);

		// Fetch media files
		var allMediaFiles = mediaFileReadService.getMediaFilesByIncidentIds(incidentIds);
		var mediaFilesGrouped = allMediaFiles.stream()
			.collect(Collectors.groupingBy(MediaFileInfo::incidentId));

		// Assemble DTOs
		return incidentInfoDtos.stream()
			.map(dto -> {
				var incident = dto.incident();
				var writer = writerMap.get(incident.getWriterId());
				var writerName = writer != null ? writer.getName() : "Unknown";
				var writerInfo = new IncidentWriterInfo(writerName);
				var mediaFiles = mediaFilesGrouped.getOrDefault(incident.getId(), Collections.emptyList());
				return new IncidentWithMediaDto(
					writerInfo,
					incident,
					dto.editable(), // 추가
					dto.liked(),   // 추가
					mediaFiles
				);
			})
			.collect(Collectors.toList());
	}

	public List<IncidentWithMediaAndDistanceDto> toIncidentWithMediaAndDistanceDtos(
		List<IncidentDistanceDto> incidentDistances
	) {
		if (incidentDistances.isEmpty()) {
			return Collections.emptyList();
		}

		// Extract incident IDs and writer IDs
		var incidentIds = incidentDistances.stream()
			.map(dto -> dto.incident().getId())
			.collect(Collectors.toList());

		var writerIds = incidentDistances.stream()
			.map(dto -> dto.incident().getWriterId())
			.distinct()
			.collect(Collectors.toList());
		var writerMap = memberService.getMembersByIds(writerIds);

		// Fetch media files
		var allMediaFiles = mediaFileReadService.getMediaFilesByIncidentIds(incidentIds);
		var mediaFilesGrouped = allMediaFiles.stream()
			.collect(Collectors.groupingBy(MediaFileInfo::incidentId));

		// Assemble DTOs
		return incidentDistances.stream()
			.map(dto -> {
				var incident = dto.incident();
				var distance = dto.distance();
				var writer = writerMap.get(incident.getWriterId());
				var writerName = writer != null ? writer.getName() : "Unknown";
				var writerInfo = new IncidentWriterInfo(writerName);
				var mediaFiles = mediaFilesGrouped.getOrDefault(incident.getId(), Collections.emptyList());
				return new IncidentWithMediaAndDistanceDto(
					writerInfo,
					incident,
					DistanceFormatter.formatDistance(distance),
					dto.editable(), // 추가
					dto.liked(),    // 추가
					mediaFiles
				);
			})
			.collect(Collectors.toList());
	}

}
