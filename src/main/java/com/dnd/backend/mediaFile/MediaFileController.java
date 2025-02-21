package com.dnd.backend.mediaFile;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dnd.backend.mediaFile.dto.UpdateMediaFileResponse;
import com.dnd.backend.mediaFile.dto.UpdateMediaFileResult;
import com.dnd.backend.mediaFile.service.MediaFileWriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/media-files")
@RequiredArgsConstructor
public class MediaFileController {

	private final MediaFileWriteService mediaFileWriteService;

	/**
	 * 게시글 이미지 수정 API
	 * PathVariable: fileUrl (기존 이미지 식별)
	 * multipart: 새 파일
	 * 응답: 새 fileUrl, updatedAt
	 */
	@PutMapping(consumes = "multipart/form-data")
	public UpdateMediaFileResponse updateMediaFile(
		@RequestParam String fileUrl,   // <-- 파일 URL을 쿼리 파라미터로
		@RequestPart("file") MultipartFile newFile
	) {
		// 1) Service 호출
		UpdateMediaFileResult result = mediaFileWriteService.updateMediaFile(fileUrl, newFile);

		// 2) Response DTO 변환
		return new UpdateMediaFileResponse(result.getNewFileUrl(), result.getUpdatedAt());
	}
}
