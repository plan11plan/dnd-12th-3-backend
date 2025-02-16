package com.dnd.backend.incident.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dnd.backend.incident.dto.WriteIncidentCommand;
import com.dnd.backend.incident.service.IncidentWriteService;
import com.dnd.backend.mediaFile.service.MediaFileWriteService;
import com.dnd.backend.user.entity.MemberEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateIncidentUseCase {
	private final IncidentWriteService incidentWriteService;
	private final MediaFileWriteService mediaFileWriteService;

	@Transactional
	public void execute(MemberEntity memberEntity, WriteIncidentCommand command, List<MultipartFile> files) {
		var incidentId = incidentWriteService.create(memberEntity.getId(), command);
		mediaFileWriteService.uploadFiles(incidentId, files);
	}
}
