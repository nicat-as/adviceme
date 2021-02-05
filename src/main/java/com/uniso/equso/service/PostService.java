package com.uniso.equso.service;


import com.uniso.equso.model.*;
import com.uniso.equso.model.posts.*;

import java.util.List;

public interface PostService {
    void createPost(CreatePostRequest request);

    PostDto getPost(Long id);

    void deletePostById(Long postId);

    PageResponse<Object> getComments(Long postId, Integer page, Integer size);

    PageResponse<List<SearchPostResponse>> searchPostByCriteria(SearchPostRequest request);
    
}
