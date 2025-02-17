package com.dnd.backend.incident;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dnd.backend.incident.application.CreateIncidentUseCase;
import com.dnd.backend.incident.application.DeleteIncidentUseCase;
import com.dnd.backend.incident.application.UpdateIncidentDescriptionUseCase;
import com.dnd.backend.incident.dto.UpdateIncidentCommand;
import com.dnd.backend.incident.dto.WriteIncidentCommand;
import com.dnd.backend.user.security.CustomeUserDetails;
import com.dnd.backend.user.security.customAuthenticationPrincipal.AuthUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incidents")
public class IncidentController {

	private final CreateIncidentUseCase createIncidentUseCase;
	private final UpdateIncidentDescriptionUseCase updateIncidentUseCase;

	private final DeleteIncidentUseCase deleteIncidentUseCase;

	@PostMapping(consumes = {"multipart/form-data"})
	public void createIncident(
		@Valid @RequestPart("incidentData") WriteIncidentCommand command,
		@RequestPart(value = "files", required = false) List<MultipartFile> files,
		@AuthUser CustomeUserDetails customeUserDetails) {

		//TODO : Null처리 DTO 캡슐화하기
		if (files == null) {
			files = Collections.emptyList();
		}
		Long memberId = customeUserDetails.getId();
		createIncidentUseCase.execute(memberId, command, files);
	}

	@PatchMapping("/{incidentId}")
	public void updateIncident(
		@PathVariable("incidentId") Long incidentId,
		@Valid @RequestBody UpdateIncidentCommand command) {
		updateIncidentUseCase.execute(incidentId, command);
	}

	@DeleteMapping("/{incidentId}")
	public void deleteIncident(
		@PathVariable("incidentId") Long incidentId) {
		deleteIncidentUseCase.execute(incidentId);
	}

}
