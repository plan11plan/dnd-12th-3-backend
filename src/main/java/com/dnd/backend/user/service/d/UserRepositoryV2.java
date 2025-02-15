package com.dnd.backend.user.service.d;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryV2 extends JpaRepository<UserEntity, Long> {
}
