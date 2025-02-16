package com.dnd.backend.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.backend.user.dto.AddressDTO;
import com.dnd.backend.user.entity.User;
import com.dnd.backend.user.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private UserServiceImpl userService;

	// 내 정보 조회
	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser() {
		User user = userService.getCurrentUser();
		return ResponseEntity.ok(user);
	}

	// 회원탈퇴
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteAccount() {
		String message = userService.deleteAccount();
		return ResponseEntity.ok(message);
	}

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@PostMapping("/address")
	public ResponseEntity<?> addAddress(@RequestBody AddressDTO addressDTO) {
		String message = userService.addAddress(addressDTO);
		return ResponseEntity.ok(message);
	}

	@DeleteMapping("/address/{addressId}")
	public ResponseEntity<?> deleteAddress(@PathVariable Long addressId) {
		String message = userService.deleteAddress(addressId);
		return ResponseEntity.ok(message);
	}
}
