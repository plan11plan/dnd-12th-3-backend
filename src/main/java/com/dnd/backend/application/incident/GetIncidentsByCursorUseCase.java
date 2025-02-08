package com.dnd.backend.application.incident;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dnd.backend.application.incident.response.IncidentCursorResponse;
import com.dnd.backend.application.incident.response.IncidentWithMediaDto;
import com.dnd.backend.domain.incidnet.entity.IncidentEntity;
import com.dnd.backend.domain.incidnet.service.IncidentReadService;
import com.dnd.backend.domain.mediaFile.dto.MediaFileInfo;
import com.dnd.backend.domain.mediaFile.service.MediaFileReadService;
import com.dnd.backend.support.util.CursorRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetIncidentsByCursorUseCase {

	private final IncidentReadService incidentReadService;
	private final MediaFileReadService mediaFileReadService;

	public IncidentCursorResponse execute(Long writerId, CursorRequest cursorRequest) {

		var incidentCursor = incidentReadService.getIncidents(writerId, cursorRequest);
		var incidents = incidentCursor.contents();

		var incidentIds = extractIncidentIds(incidents);

		var allMediaFiles = mediaFileReadService.getMediaFilesByIncidentIds(incidentIds);

		var mediaFilesGrouped = allMediaFiles.stream()
			.collect(groupingBy(MediaFileInfo::incidentId));

		var withMediaList = incidents.stream()
			.map(incident -> {
				List<MediaFileInfo> mediaFiles = mediaFilesGrouped.getOrDefault(incident.getId(), emptyList());
				return new IncidentWithMediaDto(incident, mediaFiles);
			})
			.collect(toList());

		return new IncidentCursorResponse(incidentCursor.nextCursorRequest(), withMediaList);
	}

	private List<Long> extractIncidentIds(List<IncidentEntity> contents) {
		List<Long> incidentIds = contents.stream()
			.map(IncidentEntity::getId)
			.collect(toList());
		return incidentIds;
	}
}
