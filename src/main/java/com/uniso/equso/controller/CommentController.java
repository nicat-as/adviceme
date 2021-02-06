package com.uniso.equso.controller;

import com.uniso.equso.model.comments.CommentCreateRequest;
import com.uniso.equso.model.comments.EditCommentRequest;
import com.uniso.equso.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.root}/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> addComment(@RequestBody CommentCreateRequest request) {
        commentService.createComment(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> editComment(@RequestBody EditCommentRequest comment){
        commentService.updateComment(comment);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ){
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
