package com.dnd.backend.domain.incidnet.entity.category.type;

import com.dnd.backend.domain.incidnet.entity.category.DisasterGroup;
import com.dnd.backend.domain.incidnet.entity.category.DisasterType;

public enum TerrorType implements DisasterType {
	RIOT("폭동"),
	TERROR("테러");

	private final String name;

	TerrorType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DisasterGroup getParentType() {
		return DisasterGroup.TERROR;
	}
}
