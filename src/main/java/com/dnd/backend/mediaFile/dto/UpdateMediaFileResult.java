package com.dnd.backend.mediaFile.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateMediaFileResult {
	private String newFileUrl;
	private LocalDateTime updatedAt;
}
