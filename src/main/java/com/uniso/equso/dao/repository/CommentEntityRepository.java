package com.uniso.equso.dao.repository;

import com.uniso.equso.dao.entities.CommentEntity;
import com.uniso.equso.dao.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {

    Optional<CommentEntity> findByIdAndStatus(Long id, Status status);

    Page<CommentEntity> findAllByPost_IdAndStatus(Long postId, Status status, Pageable pageable);
}
