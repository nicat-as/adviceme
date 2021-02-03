package com.uniso.equso.dao.repository.mapper;

import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.model.posts.CreatePostRequest;
import com.uniso.equso.model.posts.GetPostsRequest;
import com.uniso.equso.model.posts.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    int createPost(Long userId, CreatePostRequest request, Status status);

    Optional<PostDto> getPostById(Long userId, Long postId, Status status);

    List<PostDto> getPostsByCriteria(Long userId, GetPostsRequest criteria, boolean isZero, Status status);

//    int getPost(BigDecimal id, BigDecimal userId);

//    int getAllPost();
}
