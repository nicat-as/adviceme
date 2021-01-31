package com.uniso.equso.controller;

import com.uniso.equso.model.CreatePostRequest;
import com.uniso.equso.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("${url.root}/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody CreatePostRequest request){
        postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{id}")
    public ResponseEntity getPostById(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPost(id));
    }

}
