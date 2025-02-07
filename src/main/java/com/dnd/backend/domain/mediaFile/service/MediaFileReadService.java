package com.dnd.backend.domain.mediaFile.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dnd.backend.domain.mediaFile.entity.MediaFileEntity;
import com.dnd.backend.domain.mediaFile.repository.JpaMediaFileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaFileReadService {
	private final JpaMediaFileRepository mediaFileRepository;

	public List<MediaFileEntity> getMediaFilesByIncidentIds(List<Long> incidentIds) {
		return mediaFileRepository.findByIncidentIdIn(incidentIds);
	}
}
