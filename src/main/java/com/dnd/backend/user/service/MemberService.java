package com.dnd.backend.user.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnd.backend.support.kakao.KakaoAddressService;
import com.dnd.backend.user.dto.AddressDTO;
import com.dnd.backend.user.entity.Address;
import com.dnd.backend.user.entity.MemberEntity;
import com.dnd.backend.user.exception.BadRequestException;
import com.dnd.backend.user.exception.ResourceNotFoundException;
import com.dnd.backend.user.exception.UnauthorizedException;
import com.dnd.backend.user.repository.AddressRepository;
import com.dnd.backend.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final AddressRepository addressRepository;
	private final SecurityService securityService;
	private final KakaoAddressService kakaoAddressService;

	public MemberEntity getMember(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
	}

	public Map<Long, MemberEntity> getMembersByIds(List<Long> memberIds) {
		List<MemberEntity> members = memberRepository.findAllById(memberIds);
		return members.stream().collect(Collectors.toMap(MemberEntity::getId, member -> member));
	}
	// 현재 인증된 사용자 조회

	public MemberEntity getCurrentMember() {
		MemberEntity memberEntity = securityService.getAuthenticatedUser();
		memberEntity.setPassword(null); // 비밀번호 노출 방지
		return memberEntity;
	}

	@Transactional
	public String deleteAccount() {
		MemberEntity memberEntity = securityService.getAuthenticatedUser();
		memberRepository.delete(memberEntity);
		return "MemberEntity account deleted successfully";
	}

	@Transactional
	public String addAddress(AddressDTO addressDTO) {
		MemberEntity memberEntity = securityService.getAuthenticatedUser();
		if (memberEntity.getAddresses().size() >= 2) {
			throw new BadRequestException("Cannot add more than 2 addresses");
		}

		Address address = Address.builder()
			.addressName(addressDTO.getAddressName())
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
		List<MemberEntity> memberEntities = memberRepository.findAll();
		memberEntities.forEach(user -> user.setPassword(null));
		return memberEntities;
	}

	// public List<Address> getMyAddress() {
	// 	MemberEntity authenticatedUser = securityService.getAuthenticatedUser();
	// 	return authenticatedUser.getAddresses();
	// }
	public List<AddressDTO> getMyAddress() {
		MemberEntity authenticatedUser = securityService.getAuthenticatedUser();
		List<Address> addresses = authenticatedUser.getAddresses();

		// Address 엔티티를 AddressDTO로 변환하며, addressName을 동 단위로 변환
		return addresses.stream()
			.map(address -> {
				String dongName = kakaoAddressService.convertCoordinatesToDongName(address.getLatitude(),
					address.getLongitude());
				return AddressDTO.builder()
					.addressId(address.getId())
					.addressName(dongName) // 동 단위로 변환된 주소명
					.latitude(address.getLatitude())
					.longitude(address.getLongitude())
					.build();
			})
			.collect(Collectors.toList());
	}
}
