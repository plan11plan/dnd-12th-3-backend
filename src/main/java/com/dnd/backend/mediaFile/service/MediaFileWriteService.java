package com.dnd.backend.mediaFile.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dnd.backend.mediaFile.dto.UpdateMediaFileResult;
import com.dnd.backend.mediaFile.entity.JpaMediaFileRepository;
import com.dnd.backend.mediaFile.entity.MediaFileEntity;
import com.dnd.backend.mediaFile.entity.MediaType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaFileWriteService {
	private final CloudStorageService cloudStorageService;
	private final JpaMediaFileRepository mediaFileRepository;

	public void uploadFiles(Long incidentId, List<MultipartFile> files) {

		int currentFileCount = mediaFileRepository.countByIncidentId(incidentId);
		validateMaximumUpload(files, currentFileCount);

		for (MultipartFile file : files) {
			String fileUrl = cloudStorageService.uploadFile(file);
			MediaType mediaType = determineFileType(file);

			MediaFileEntity fileEntity = MediaFileEntity.builder()
				.incidentId(incidentId)
				.fileType(mediaType)
				.fileUrl(fileUrl)
				.originalFilename(file.getOriginalFilename())
				.build();
			mediaFileRepository.save(fileEntity);
		}
	}

	private void validateMaximumUpload(List<MultipartFile> files, int currentFileCount) {
		if (currentFileCount + files.size() > 3) {
			throw new IllegalArgumentException("파일은 최대 3개까지 업로드 가능합니다.");
		}
	}

	private MediaType determineFileType(MultipartFile file) {
		String contentType = file.getContentType();
		//TODO: 영상 확장자 로직 개선 필요
		if (contentType != null && contentType.startsWith("video")) {
			return MediaType.VIDEO;
		}
		return MediaType.IMAGE;
	}

	/**
	 * 기존 fileUrl에 해당하는 이미지(또는 동영상)를 새로 업데이트
	 * 1) DB에서 fileUrl 검색 -> 없으면 예외
	 * 2) (선택) 기존 S3 파일 제거
	 * 3) 새 파일 S3 업로드
	 * 4) DB 엔티티 갱신 (fileUrl, originalFilename, fileType)
	 * 5) 반환: 새로운 fileUrl, updatedAt
	 */
	public UpdateMediaFileResult updateMediaFile(String oldFileUrl, MultipartFile newFile) {
		// 1) DB 조회
		MediaFileEntity mediaFileEntity = mediaFileRepository.findByFileUrl(oldFileUrl)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 fileUrl 입니다. fileUrl=" + oldFileUrl));

		// 2) (옵션) 기존 S3 파일 삭제
		//    key 추출해야 하지만, 여기서는 간단히 oldFileUrl 전체로 파싱하지 않고
		//    필요시 cloudStorageService.deleteFile(key) 등의 로직 추가
		//    e.g. String oldKey = extractKeyFromUrl(oldFileUrl);
		//    cloudStorageService.deleteFile(oldKey);

		// 3) 새 파일 업로드
		String newUrl = cloudStorageService.uploadFile(newFile);
		MediaType newType = determineFileType(newFile);
		String newFilename = newFile.getOriginalFilename();

		// 4) 엔티티 필드 변경
		mediaFileEntity.updateFile(newUrl, newFilename, newType);

		// 영속성 컨텍스트가 변경을 감지하고, BaseTimeEntity 의 updatedAt 갱신
		mediaFileRepository.save(mediaFileEntity);

		// 5) 결과로 새로운 fileUrl 및 updatedAt 반환
		return new UpdateMediaFileResult(
			mediaFileEntity.getFileUrl(),
			mediaFileEntity.getUpdatedAt()  // BaseTimeEntity가 있다면 자동 갱신
		);
	}
}
