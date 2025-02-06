package com.dnd.backend.domain.incidnet.entity.category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dnd.backend.domain.incidnet.entity.category.type.CollapseType;
import com.dnd.backend.domain.incidnet.entity.category.type.ExplosionType;
import com.dnd.backend.domain.incidnet.entity.category.type.FineDustType;
import com.dnd.backend.domain.incidnet.entity.category.type.FireType;
import com.dnd.backend.domain.incidnet.entity.category.type.NaturalDisasterType;
import com.dnd.backend.domain.incidnet.entity.category.type.TerrorType;
import com.dnd.backend.domain.incidnet.entity.category.type.TrafficType;

public enum DisasterGroup {
	TRAFFIC("교통", List.of(TrafficType.values())),
	FIRE("화재", List.of(FireType.values())),
	COLLAPSE("붕괴", List.of(CollapseType.values())),
	EXPLOSION("폭발", List.of(ExplosionType.values())),
	NATURAL("자연재난", List.of(NaturalDisasterType.values())),
	FINE_DUST("미세먼지", List.of(FineDustType.values())),
	TERROR("테러", List.of(TerrorType.values())),
	EMPTY("없음", Collections.EMPTY_LIST);

	private final String name;
	private final Map<String, DisasterType> subTypeMap;

	DisasterGroup(String name, List<DisasterType> subTypes) {
		this.name = name;
		this.subTypeMap = subTypes.stream()
			.collect(Collectors.toMap(DisasterType::getName, Function.identity()));
	}

	public static DisasterGroup mapToDisasterGroup(String groupName) {
		return Arrays.stream(DisasterGroup.values())
			.filter(group -> group.getName().equals(groupName))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 재난 그룹: " + groupName));
	}

	public Optional<DisasterType> getDisasterSubType(String name) {
		return Optional.ofNullable(subTypeMap.get(name));
	}

	public boolean hasDisasterSubType(DisasterType disasterType) {
		return subTypeMap.containsValue(disasterType);
	}

	public String getName() {
		return name;
	}

	public List<DisasterType> getSubTypes() {
		return new ArrayList<>(subTypeMap.values());
	}
}
