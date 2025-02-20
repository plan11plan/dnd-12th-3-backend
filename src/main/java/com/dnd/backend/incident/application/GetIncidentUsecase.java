package com.dnd.backend.incident.application;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dnd.backend.incident.dto.IncidentDto;
import com.dnd.backend.incident.dto.IncidentInfoDto;
import com.dnd.backend.incident.service.IncidentReadService;
import com.dnd.backend.mediaFile.dto.MediaFileInfo;
import com.dnd.backend.mediaFile.service.MediaFileReadService;
import com.dnd.backend.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetIncidentUsecase {
	private final IncidentReadService incidentReadService;
	private final MemberService memberService;
	private final MediaFileReadService mediaFileReadService;

	public IncidentDto execute(Long incidentId) {
		IncidentInfoDto incident = incidentReadService.getIncident(incidentId);
		var writerId = incident.incidentEntity().getWriterId();
		var writerName = memberService.getMember(writerId).getName();
		List<MediaFileInfo> mediaFilesByIncidentIds = mediaFileReadService.getMediaFilesByIncidentIds(
			List.of(incidentId));
		return IncidentDto.from(
			incidentReadService.getIncident(incidentId).incidentEntity(),
			incident.liked(),
			incident.editable(),
			writerName,
			mediaFilesByIncidentIds);
	}
}
