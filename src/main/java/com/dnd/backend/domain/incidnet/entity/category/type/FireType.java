package com.dnd.backend.domain.incidnet.entity.category.type;

import com.dnd.backend.domain.incidnet.entity.category.DisasterGroup;
import com.dnd.backend.domain.incidnet.entity.category.DisasterType;

public enum FireType implements DisasterType {
	NATURAL("자연");

	private final String name;

	FireType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DisasterGroup getParentType() {
		return DisasterGroup.화재;
	}
}
