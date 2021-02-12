package com.uniso.equso.service;

import com.uniso.equso.model.comments.CommentCreateRequest;
import com.uniso.equso.model.comments.EditCommentRequest;

import java.util.Map;

public interface CommentService {

    Map<String,Long> createComment(CommentCreateRequest request);

    void updateComment(EditCommentRequest request);

    void deleteComment(Long commentId);
}
