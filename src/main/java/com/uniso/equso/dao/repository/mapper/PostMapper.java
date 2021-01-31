package com.uniso.equso.dao.repository.mapper;

import com.uniso.equso.model.CreatePostRequest;
import com.uniso.equso.model.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.text.Bidi;
import java.util.Optional;

@Mapper
public interface PostMapper {

    int createPost(Long userId, CreatePostRequest request);

    Optional<PostDto> getPostById(Long userId, Long postId);

//    int getPost(BigDecimal id, BigDecimal userId);

//    int getAllPost();
}
