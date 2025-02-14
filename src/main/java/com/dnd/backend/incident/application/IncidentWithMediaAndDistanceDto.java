package com.dnd.backend.incident.application;

import java.util.List;

import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.mediaFile.dto.MediaFileInfo;

public record IncidentWithMediaAndDistanceDto(
	IncidentWriterInfo writer,
	IncidentEntity incident,
	String distance,
	List<MediaFileInfo> mediaFiles) {
}
