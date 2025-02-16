package com.dnd.backend.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.backend.user.entity.Address;
import com.dnd.backend.user.entity.MemberEntity;

public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findByUser(MemberEntity memberEntity);
}
