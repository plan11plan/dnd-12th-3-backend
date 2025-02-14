package com.dnd.backend.domain.incident.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "incident_likes")
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IncidentLikeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long writerId;

	private Long incidentId;

	@Enumerated(EnumType.STRING)
	private LikeStatus likeStatus;

	private LocalDateTime createdAt;

	@Builder
	public IncidentLikeEntity(Long writerId, Long incidentId, LikeStatus likeStatus) {
		this.writerId = writerId;
		this.incidentId = incidentId;
		this.likeStatus = likeStatus;
		this.createdAt = LocalDateTime.now();
	}
}
