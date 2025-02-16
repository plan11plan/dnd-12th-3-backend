package com.dnd.backend.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dnd.backend.user.entity.MemberEntity;

@Repository
public interface UserRepository extends JpaRepository<MemberEntity, Long> {
	Optional<MemberEntity> findByEmail(String email);
}
