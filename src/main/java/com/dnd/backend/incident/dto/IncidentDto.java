package com.dnd.backend.incident.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dnd.backend.incident.entity.IncidentEntity;
import com.dnd.backend.incident.entity.category.IncidentCategory;
import com.dnd.backend.mediaFile.dto.MediaFileInfo;
import com.dnd.backend.support.util.DistanceFormatter;

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
	String roadNameAddress,
	String lotNumberAddress,
	boolean liked,
	boolean editable,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	List<MediaFileInfo> mediaFiles,
	String distance  // 추가
) {
	// 기존 from() 메서드 오버로드 or 수정
	public static IncidentDto from(IncidentEntity incident,
		boolean liked,
		boolean editable,
		String writerName,
		List<MediaFileInfo> mediaFiles,
		double distance) {
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
			incident.getRoadNameAddress(),
			incident.getLotNumberAddress(),
			liked,
			editable,
			incident.getCreatedAt(),
			incident.getUpdatedAt(),
			mediaFiles,
			DistanceFormatter.formatDistance(distance)
		);
	}
}
