package com.dnd.backend.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAddressDTO {
	private Long addressId;
	private String addressName; // 도로명주소
	// 시도명
	private String sido;
	// 시군구명
	private String sgg;
	//읍면도명
	private String emd;
	private double latitude;
	private double longitude;
}
