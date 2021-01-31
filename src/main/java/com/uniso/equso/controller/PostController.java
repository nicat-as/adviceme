package com.uniso.equso.controller;

import com.uniso.equso.model.CreatePostRequest;
import com.uniso.equso.model.GetPostsRequest;
import com.uniso.equso.model.GetPostsResponse;
import com.uniso.equso.model.PostDto;
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
    public ResponseEntity<Void> createPost(@RequestBody CreatePostRequest request){
        postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId){
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PostMapping("find")
    public ResponseEntity<GetPostsResponse> getPosts(@RequestBody GetPostsRequest request){
        return ResponseEntity.ok(postService.getPosts(request));
    }

}
