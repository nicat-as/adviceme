package com.uniso.equso.service;


import com.uniso.equso.model.CreatePostRequest;
import com.uniso.equso.model.PostDto;

public interface PostService {
    void createPost(CreatePostRequest request);

    PostDto getPost(Long id);
}
