package com.dnd.backend.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.user.dto.AddressDTO;
import com.dnd.backend.user.entity.Address;
import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.exception.ResourceNotFoundException;
import com.dnd.backend.user.exception.UnauthorizedException;
import com.dnd.backend.user.repository.AddressRepository;
import com.dnd.backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final SecurityService securityService;

	// 현재 인증된 사용자 조회

	public MemberEntity getCurrentUser() {
		MemberEntity memberEntity = securityService.getAuthenticatedUser();
		memberEntity.setPassword(null); // 비밀번호 노출 방지
		return memberEntity;
	}

	@Transactional
	public String deleteAccount() {
		MemberEntity memberEntity = securityService.getAuthenticatedUser();
		userRepository.delete(memberEntity);
		return "MemberEntity account deleted successfully";
	}

	@Transactional
	public String addAddress(AddressDTO addressDTO) {
		MemberEntity memberEntity = securityService.getAuthenticatedUser();
		if (memberEntity.getAddresses().size() >= 2) {
			throw new BadRequestException("Cannot add more than 2 addresses");
		}

		Address address = Address.builder()
			.title(addressDTO.getTitle())
			.latitude(addressDTO.getLatitude())
			.longitude(addressDTO.getLongitude())
			.memberEntity(memberEntity)
			.build();

		memberEntity.addAddress(address);
		addressRepository.save(address);
		return "Address added successfully";
	}

	@Transactional
	public String deleteAddress(Long addressId) {
		MemberEntity memberEntity = securityService.getAuthenticatedUser();
		Address address = addressRepository.findById(addressId)
			.orElseThrow(() -> new ResourceNotFoundException("Address not found"));

		if (!address.getMemberEntity().getId().equals(memberEntity.getId())) {
			throw new UnauthorizedException("Not authorized to delete this address");
		}

		memberEntity.removeAddress(address);
		addressRepository.delete(address);
		return "Address deleted successfully";

	}

	public List<MemberEntity> getAllUsers() {
		List<MemberEntity> memberEntities = userRepository.findAll();
		memberEntities.forEach(user -> user.setPassword(null));
		return memberEntities;
	}
}
