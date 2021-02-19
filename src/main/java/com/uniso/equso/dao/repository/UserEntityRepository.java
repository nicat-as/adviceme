package com.uniso.equso.dao.repository;

import com.uniso.equso.dao.entities.UserEntity;
import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.dao.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    Boolean existsByEmail(String email);

    Page<UserEntity> findAllByTypeAndStatus(UserType type, Status status, Pageable pageable);

    Optional<UserEntity> findByIdAndStatus(Long id, Status status);
}
