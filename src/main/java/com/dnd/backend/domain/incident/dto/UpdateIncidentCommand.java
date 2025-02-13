package com.dnd.backend.domain.incident.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateIncidentCommand(
	@NotBlank(message = "본문을 입력해주세요.")
	@Size(min = 1, max = 1000, message = "본문은 1자 이상 1000자 이하로 입력해주세요.")
	String newDescription,

	@NotBlank(message = "위치 정보를 입력해주세요.")
	String newLocationName,

	@NotBlank(message = "재난 그룹을 선택해주세요.")
	String newDisasterGroup
) {

}

