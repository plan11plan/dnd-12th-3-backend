package com.dnd.backend.user.dto;

import lombok.Data;

@Data
public class AddressDTO {
	private String addressName;
	private double latitude;
	private double longitude;
}
