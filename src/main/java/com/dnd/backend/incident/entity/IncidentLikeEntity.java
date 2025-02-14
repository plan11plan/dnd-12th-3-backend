package com.dnd.backend.incident.entity;

import com.dnd.backend.support.auditing.BaseTimeEntity;

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
public class IncidentLikeEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long writerId;

	private Long incidentId;

	@Enumerated(EnumType.STRING)
	private LikeStatus likeStatus;

	@Builder
	public IncidentLikeEntity(Long writerId, Long incidentId, LikeStatus likeStatus) {
		this.writerId = writerId;
		this.incidentId = incidentId;
		this.likeStatus = likeStatus;
	}

	public void setLikeStatus(LikeStatus likeStatus) {
		this.likeStatus = likeStatus;
	}

	public void toLike() {
		this.likeStatus = LikeStatus.LIKE;
	}

	public void toUnLike() {
		this.likeStatus = LikeStatus.UNLIKE;
	}

	public boolean isLike() {
		return this.likeStatus == LikeStatus.LIKE;
	}
}
