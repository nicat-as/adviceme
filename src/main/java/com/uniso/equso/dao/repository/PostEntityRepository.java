package com.uniso.equso.dao.repository;

import com.uniso.equso.dao.entities.PostEntity;
import com.uniso.equso.dao.enums.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {
    Optional<PostEntity> findByIdAndStatus(Long id, Status status);

    List<PostEntity> findAll(@Nullable Specification<PostEntity> specification, Sort sort);
}
