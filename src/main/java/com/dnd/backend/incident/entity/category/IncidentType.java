package com.dnd.backend.incident.entity.category;

// 하위 유형들을 위한 공통 인터페이스
public interface IncidentType {
	String getName();

	IncidentCategory getParentType();
}
