package com.uniso.equso.service;


import com.uniso.equso.model.CreatePostRequest;
import com.uniso.equso.model.GetPostsRequest;
import com.uniso.equso.model.GetPostsResponse;
import com.uniso.equso.model.PostDto;

import java.util.List;

public interface PostService {
    void createPost(CreatePostRequest request);

    PostDto getPost(Long id);

    GetPostsResponse getPosts(GetPostsRequest request);
}
