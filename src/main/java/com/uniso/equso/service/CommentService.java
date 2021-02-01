package com.uniso.equso.service;

import com.uniso.equso.model.CommentCreateRequest;
import com.uniso.equso.model.EditCommentRequest;

public interface CommentService {

    void createComment(CommentCreateRequest request);

    void updateComment(EditCommentRequest request);
}
