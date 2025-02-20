package com.dnd.backend.incident.application.response;

import java.util.List;

import com.dnd.backend.incident.application.IncidentWriterInfo;
import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.mediaFile.dto.MediaFileInfo;

public record IncidentWithMediaDto(
	IncidentWriterInfo writer,
	IncidentEntity incident,
	boolean editable, // 추가
	boolean liked,    // 추가
	List<MediaFileInfo> mediaFiles
) {
}
