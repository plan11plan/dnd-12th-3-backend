package com.dnd.backend.mediaFile.dto;

public record MediaFileInfo(
	Long incidentId,
	String mediaType,
	String fileUrl) {
}
