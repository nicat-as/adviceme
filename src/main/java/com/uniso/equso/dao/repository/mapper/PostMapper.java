package com.uniso.equso.dao.repository.mapper;

import com.uniso.equso.model.CreatePostRequest;
import com.uniso.equso.model.GetPostsRequest;
import com.uniso.equso.model.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.text.Bidi;
import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    int createPost(Long userId, CreatePostRequest request);

    Optional<PostDto> getPostById(Long userId, Long postId);

    List<PostDto> getPostsByCriteria(Long userId, GetPostsRequest criteria,boolean isZero);

//    int getPost(BigDecimal id, BigDecimal userId);

//    int getAllPost();
}
