package com.dnd.backend.comment;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponse {
	private Long commentId;
	private String content;
	private String writerName;
	private boolean editable;
	private LocalDateTime createdAt;
	private Long parentId;
}
