package com.uniso.equso.controller;

import com.uniso.equso.model.*;
import com.uniso.equso.model.posts.*;
import com.uniso.equso.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${url.root}/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody CreatePostRequest request) {
        postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePostById(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("{postId}/comment")
    public ResponseEntity<PageResponse<?>> getCommentsByPost(@PathVariable Long postId,
                                                                      @RequestParam("page") Integer page,
                                                                      @RequestParam("size") Integer size) {
        return ResponseEntity.ok(postService.getComments(postId, page, size));
    }

    @PostMapping("search")
    public ResponseEntity<PageResponse<List<SearchPostResponse>>> searchByCriteria(
            @RequestBody SearchPostRequest request
    ){
        return ResponseEntity.ok(postService.searchPostByCriteria(request));
    }

}
