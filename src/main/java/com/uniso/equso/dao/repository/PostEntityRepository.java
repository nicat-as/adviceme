package com.uniso.equso.dao.repository;

import com.uniso.equso.dao.entities.PostEntity;
import com.uniso.equso.dao.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {

    Optional<PostEntity> findByIdAndStatus(Long id, Status status);

    @Query(value = "select * from post p " +
            " left join comment c on c.post_id=p.id " +
            " inner join \"user\" wu on wu.id=p.wall_user_id "+
            " inner join \"user\" cr on cr.id=p.creator_id "+
            " inner join post_categories pc on pc.id=p.post_category_id "+
            " where p.id =:id and p.status=:status " +
            " limit :limit offset :offset",
            nativeQuery = true
    )
    Optional<PostEntity> getPostByIdAndStatus(@Param("id") Long id,
                                              @Param("status") String status,
                                              @Param("limit") Integer limit,
                                              @Param("offset") Integer offset);


    Page<PostEntity> findAll(@Nullable Specification<PostEntity> specification, Pageable pageable);

}
