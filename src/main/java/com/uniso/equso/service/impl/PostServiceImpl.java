package com.uniso.equso.service.impl;

import com.uniso.equso.config.security.CustomUserDetails;
import com.uniso.equso.dao.repository.mapper.PostMapper;
import com.uniso.equso.exceptions.PostException;
import com.uniso.equso.model.CreatePostRequest;
import com.uniso.equso.model.GetPostsRequest;
import com.uniso.equso.model.GetPostsResponse;
import com.uniso.equso.model.PostDto;
import com.uniso.equso.service.PostService;
import com.uniso.equso.util.AuthenticationUtil;
import com.uniso.equso.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final AuthenticationUtil authenticationUtil;

    @Override
    public void createPost(CreatePostRequest request) {
        var userId = getUserId();
        validateAndFillWallUser(request,userId);
        if(postMapper.createPost(getUserId(),request) <= 0){
            log.error("POST NOT CREATED");
            throw new PostException("exception.post-not-created");
        }
    }

    @Override
    public PostDto getPost(Long postId) {
       return postMapper.getPostById(getUserId(),postId)
               .orElseThrow(() -> new PostException("exception.post-not-found"));
    }

    @Override
    public GetPostsResponse getPosts(GetPostsRequest criteria) {
        Integer maxCount = criteria.getMaxCount();
        boolean isZero = maxCount == 0;
        boolean hasMore = false;
        criteria.setPage((criteria.getPage()-1) * (isZero?10:criteria.getMaxCount()));
        criteria.setMaxCount(maxCount + 1);
        List<PostDto> postDtoList =  postMapper.getPostsByCriteria(getUserId(),criteria,isZero);
        if(postDtoList.size() - 1 == maxCount && !isZero){
           hasMore = true;
            postDtoList.remove(postDtoList.size() -1);
        }
        return GetPostsResponse.builder()
                .posts(postDtoList)
                .hasMore(hasMore)
                .build();
    }

    private Long getUserId(){
        return authenticationUtil.getUserDetail().getUserEntity().getId();
    }

    private void validateAndFillWallUser(CreatePostRequest request,Long userId) {
        if(request.getWallUserId() == null){
            request.setWallUserId(userId);
        }
    }
}
