package com.uniso.equso.dao.repository;

import com.uniso.equso.dao.entities.PostEntity;
import com.uniso.equso.dao.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findByIdAndStatus(Long id, Status status);
}
