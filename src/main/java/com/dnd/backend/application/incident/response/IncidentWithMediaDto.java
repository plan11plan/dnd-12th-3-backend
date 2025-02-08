package com.dnd.backend.application.incident.response;

import java.util.List;

import com.dnd.backend.domain.incidnet.entity.IncidentEntity;
import com.dnd.backend.domain.mediaFile.entity.MediaFileEntity;

public record IncidentWithMediaDto(
	IncidentEntity incident,
	List<MediaFileEntity> mediaFiles
) {
}
