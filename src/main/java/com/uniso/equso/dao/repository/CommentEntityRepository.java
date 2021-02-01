package com.uniso.equso.dao.repository;

import com.uniso.equso.dao.entities.CommentEntity;
import com.uniso.equso.dao.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByPost_IdAndStatus(Long postId, Status status, Pageable pageable);
}
