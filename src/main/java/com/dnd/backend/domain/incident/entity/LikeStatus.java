package com.dnd.backend.domain.incident.entity;

import lombok.Getter;

@Getter
public enum LikeStatus {
	LIKE(1, "Like"),
	UNLIKE(0, "CancelLike/Not Like");

	private int code;
	private String msg;

	LikeStatus(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
