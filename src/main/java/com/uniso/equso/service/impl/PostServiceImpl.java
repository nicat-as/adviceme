package com.uniso.equso.service.impl;

import com.uniso.equso.config.security.CustomUserDetails;
import com.uniso.equso.dao.repository.mapper.PostMapper;
import com.uniso.equso.exceptions.PostException;
import com.uniso.equso.model.CreatePostRequest;
import com.uniso.equso.model.PostDto;
import com.uniso.equso.service.PostService;
import com.uniso.equso.util.AuthenticationUtil;
import com.uniso.equso.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final AuthenticationUtil authenticationUtil;

    @Override
    public void createPost(CreatePostRequest request) {

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

    private Long getUserId(){
        return authenticationUtil.getUserDetail().getUserEntity().getId();
    }
}
