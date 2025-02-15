package com.dnd.backend.user.dto;

import lombok.Data;

@Data
public class AddressDTO {
	// 조회/삭제용 (등록 시엔 무시)
	private Long id;
	private String title;
	private double latitude;
	private double longitude;
}
