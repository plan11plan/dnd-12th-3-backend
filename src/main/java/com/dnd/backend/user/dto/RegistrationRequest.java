package com.dnd.backend.user.dto;

import lombok.Data;

@Data
public class RegistrationRequest {
	private String email;
	private String password;
}
