package com.dnd.backend.mediaFile.entity;

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
@Table(name = "media_files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MediaFileEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private Long incidentId;

	@NotNull
	@Enumerated(EnumType.STRING)
	private MediaType fileType;

	@NotNull
	private String fileUrl;

	private String originalFilename;

	// 파일 수정 시 사용
	public void updateFile(String newFileUrl, String newOriginalFilename, MediaType newMediaType) {
		this.fileUrl = newFileUrl;
		this.originalFilename = newOriginalFilename;
		this.fileType = newMediaType;
	}
}
