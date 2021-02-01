package com.uniso.equso.service.impl;

import com.uniso.equso.dao.entities.CommentEntity;
import com.uniso.equso.dao.entities.UserEntity;
import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.dao.repository.CommentEntityRepository;
import com.uniso.equso.dao.repository.PostEntityRepository;
import com.uniso.equso.exceptions.AuthorizationException;
import com.uniso.equso.exceptions.CommentException;
import com.uniso.equso.exceptions.PostException;
import com.uniso.equso.model.CommentCreateRequest;
import com.uniso.equso.model.EditCommentRequest;
import com.uniso.equso.service.CommentService;
import com.uniso.equso.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentEntityRepository commentEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final AuthenticationUtil authenticationUtil;


    @Override
    public void createComment(CommentCreateRequest request) {
        log.info("ActionLog.createComment.started for postId:{}", request.getPostId());
        var post = postEntityRepository.findById(request.getPostId())
                .orElseThrow(() -> {
                    log.error("Post not found");
                    throw new PostException("exception.post-not-found");
                });

        var comment = CommentEntity.builder()
                .creator(UserEntity.builder()
                        .id(authenticationUtil.getUserDetail().getUserEntity().getId())
                        .build())
                .text(request.getText())
                .post(post)
                .status(Status.ACTIVE)
                .build();

        commentEntityRepository.save(comment);

        log.info("ActionLog.createComment.started");
    }

    @Override
    public void updateComment(EditCommentRequest request) {
        log.info("ActionLog.updateComment.started for comment:{}", request.getCommentId());
        var comment = commentEntityRepository.findById(request.getCommentId())
                .orElseThrow(() -> new CommentException("exception.comment-not-found"));

        if (comment.getCreator().getId() != authenticationUtil.getUserDetail().getUserEntity().getId()) {
            log.error("Edit comment:{} for user:{} is not authorized", request.getCommentId(),
                    authenticationUtil.getUserDetail().getUserEntity().getId());
            throw new AuthorizationException("exception.authorization.comment-edit");
        }

        comment.setText(request.getNewText());

        commentEntityRepository.save(comment);

        log.info("ActionLog.updateComment.ended");
    }
}
