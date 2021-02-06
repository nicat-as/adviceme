package com.uniso.equso.service;

import com.uniso.equso.model.comments.CommentCreateRequest;
import com.uniso.equso.model.comments.EditCommentRequest;

public interface CommentService {

    void createComment(CommentCreateRequest request);

    void updateComment(EditCommentRequest request);

    void deleteComment(Long commentId);
}
