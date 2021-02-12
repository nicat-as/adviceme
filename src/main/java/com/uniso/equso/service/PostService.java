package com.uniso.equso.service;


import com.uniso.equso.model.*;
import com.uniso.equso.model.posts.*;

import java.util.List;
import java.util.Map;

public interface PostService {
    Map<String,Long> createPost(CreatePostRequest request);

    PostDto getPost(Long id);

    void deletePostById(Long postId);

    PageResponse<Object> getComments(Long postId, Integer page, Integer size);

    PageResponse<List<SearchPostResponse>> searchPostByCriteria(SearchPostRequest request);

    PostDto editPost(EditPostRequest request);
}
