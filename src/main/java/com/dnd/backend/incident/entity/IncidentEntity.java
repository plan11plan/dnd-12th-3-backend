package com.dnd.backend.incident.entity;

import java.util.Objects;

import com.dnd.backend.incident.entity.category.IncidentCategory;
import com.dnd.backend.support.auditing.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "incidents")
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IncidentEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long writerId;

	@NotNull
	private String description;

	@Enumerated(EnumType.STRING)
	private IncidentCategory incidentCategory;

	private double latitude;

	private double longitude;
	private String locationInfoName;

	private int commentCount;
	private int likeCount; // 좋아요 수 필드 추가

	@Builder
	public IncidentEntity(
		Long writerId,
		String description,
		IncidentCategory incidentCategory,
		double latitude,
		double longitude,
		String locationInfoName
	) {
		this.writerId = Objects.requireNonNull(writerId);
		this.description = Objects.requireNonNull(description);
		this.incidentCategory = Objects.requireNonNull(incidentCategory);
		this.latitude = latitude;
		this.longitude = longitude;
		this.locationInfoName = locationInfoName;
		this.commentCount = 0;
		this.likeCount = 0;
	}

	public final IncidentEntity updateDetails(
		String description,
		String roadNameAddress,
		IncidentCategory incidentCategory
	) {
		return this.toBuilder()
			.description(description)
			.locationInfoName(roadNameAddress)
			.incidentCategory(incidentCategory)
			.build();
	}

	// 좋아요 수 업데이트 메서드 추가
	public void updateLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
}
