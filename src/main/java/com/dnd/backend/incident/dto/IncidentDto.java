package com.dnd.backend.incident.dto;

import java.util.List;

import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.entity.category.IncidentCategory;
import com.dnd.backend.mediaFile.dto.MediaFileInfo;

public record IncidentDto(
	Long id,
	Long writerId,
	String writerName,

	String description,
	IncidentCategory incidentCategory,
	double latitude,
	double longitude,
	String locationInfoName,
	int likeCount,
	int commentCount,
	List<MediaFileInfo> mediaFiles
) {
	// Entity -> DTO 변환을 위한 factory 메서드
	public static IncidentDto from(IncidentEntity incident, String writerName,
		List<MediaFileInfo> mediaFiles) {
		return new IncidentDto(
			incident.getId(),
			incident.getWriterId(),
			writerName,
			incident.getDescription(),
			incident.getIncidentCategory(),
			incident.getLatitude(),
			incident.getLongitude(),
			incident.getLocationInfoName(),
			incident.getLikeCount(),
			incident.getCommentCount(),
			mediaFiles
		);
	}
}
