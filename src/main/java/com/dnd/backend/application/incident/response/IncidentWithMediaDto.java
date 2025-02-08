package com.dnd.backend.application.incident.response;

import java.util.List;

import com.dnd.backend.domain.incidnet.entity.IncidentEntity;
import com.dnd.backend.domain.mediaFile.dto.MediaFileInfo;

public record IncidentWithMediaDto(
	IncidentEntity incident,
	List<MediaFileInfo> mediaFiles
) {
}
