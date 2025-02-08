package com.dnd.backend.domain.incidnet.entity.category;

// 하위 유형들을 위한 공통 인터페이스
public interface DisasterType {
	String getName();

	DisasterGroup getParentType();
}
