package com.dnd.backend.mediaFile.dto;

import java.time.LocalDateTime;

public record UpdateMediaFileResponse(
	String fileUrl,
	LocalDateTime updatedAt
) {
}
