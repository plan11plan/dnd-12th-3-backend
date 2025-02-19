package com.dnd.backend.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDTO {
	private Long addressId;
	private String addressName;
	private double latitude;
	private double longitude;
}
