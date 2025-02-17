package com.dnd.backend.incident.dto;

import com.dnd.backend.incident.entity.category.ValidIncidentCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateIncidentCommand(
	@NotBlank(message = "내용을 입력해주세요.")
	@Size(min = 1, max = 1000, message = "내용은 1자 이상 1000자 이하로 입력해주세요.")
	String description,

	@NotBlank(message = "위치 정보를 입력해주세요.")
	@Size(min = 1, max = 100, message = "위치 정보는 1자 이상 100자 이하로 입력해주세요.")
	String locationInfoName,

	@NotBlank(message = "재난 그룹을 선택해주세요.")
	@ValidIncidentCategory
	String incidentCategory
) {

}

