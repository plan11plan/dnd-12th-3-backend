package com.dnd.backend.domain.incidnet.entity.category.type;

import com.dnd.backend.domain.incidnet.entity.category.DisasterGroup;
import com.dnd.backend.domain.incidnet.entity.category.DisasterType;

public enum ExplosionType implements DisasterType {
	EXPLOSION("폭발");

	private final String name;

	ExplosionType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DisasterGroup getParentType() {
		return DisasterGroup.폭발;
	}
}
