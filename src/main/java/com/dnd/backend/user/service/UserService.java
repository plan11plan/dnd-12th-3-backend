package com.dnd.backend.user.service;

import java.util.List;

import com.dnd.backend.user.dto.AddressDTO;
import com.dnd.backend.user.entity.User;

public interface UserService {
	User getCurrentUser();

	String deleteAccount();

	String addAddress(AddressDTO addressDTO);

	String deleteAddress(Long addressId);

	List<User> getAllUsers();

}
