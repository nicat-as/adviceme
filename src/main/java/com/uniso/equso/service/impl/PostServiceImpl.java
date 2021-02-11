package com.uniso.equso.service.impl;

import com.uniso.equso.dao.entities.CategoryEntity;
import com.uniso.equso.dao.entities.PostEntity;
import com.uniso.equso.dao.entities.UserEntity;
import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.dao.repository.CommentEntityRepository;
import com.uniso.equso.dao.repository.PostEntityRepository;
import com.uniso.equso.exceptions.AuthorizationException;
import com.uniso.equso.exceptions.PostException;
import com.uniso.equso.mapper.CommentMapper;
import com.uniso.equso.mapper.PostMapper;
import com.uniso.equso.model.PageResponse;
import com.uniso.equso.model.posts.*;
import com.uniso.equso.service.PostService;
import com.uniso.equso.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.uniso.equso.dao.spec.PostEntitySpecification.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final AuthenticationUtil authenticationUtil;
    private final PostEntityRepository postEntityRepository;
    private final CommentEntityRepository commentEntityRepository;

    @Override
    public void createPost(CreatePostRequest request) {
        log.info("ActionLog.createPost.start");

        var userId = getUserId();

        validateAndFillWallUser(request, userId);

        var wallUserId = request.getWallUserId() == null ? userId : request.getWallUserId();

        var newPost = PostEntity.builder()
                .text(request.getText())
                .wallUser(UserEntity.builder().id(wallUserId).build())
                .creator(UserEntity.builder().id(userId).build())
                .category(CategoryEntity.builder().id(request.getCategoryId()).build())
                .status(Status.ACTIVE)
                .build();

        postEntityRepository.save(newPost);

        log.debug("Saved post for user: {}, post: {}", userId, newPost.getId());

        log.info("ActionLog.createPost.end");
    }

    @Override
    public PostDto getPost(Long postId) {
        log.info("ActionLog.getPost.start for post:{}", postId);

        var offset = 0;
        var limit = 10;

        var post = postEntityRepository.getPostByIdAndStatus(postId,
                Status.ACTIVE.name(),
                limit,
                offset
        ).orElseThrow(() -> new PostException("exception.post-not-found"));

        log.debug("Post: {}", post);

        log.info("ActionLog.getPost.end for post:{}", postId);

        return PostMapper.INSTANCE.entityPostDto(post);
    }

    @Override
    public void deletePostById(Long postId) {
        log.info("ActionLog.deletePostById.started for post:{}", postId);
        var post = postEntityRepository.findByIdAndStatus(postId, Status.ACTIVE)
                .orElseThrow(() -> {
                    log.error("exception.post-not-found");
                    throw new PostException("exception.post-not-found");
                });

        if (!post.getCreator().getId().equals(getUserId())) {
            log.error("Delete post in not authorized for user: {}", post.getCreator().getId());
            throw new AuthorizationException("exception.authorization.delete-post");
        }
        post.setStatus(Status.DEACTIVE);
        postEntityRepository.save(post);

        log.info("ActionLog.deletePostById.ended");
    }

    @Override
    public PageResponse<Object> getComments(Long postId, Integer page, Integer size) {
        log.info("ActionLog.getComments.started for post:{} | page:{} | size:{}", postId, page, size);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("lastUpdatedAt").descending());

        var commentPage = commentEntityRepository
                .findAllByPost_IdAndStatus(postId, Status.ACTIVE, pageable);


        log.info("ActionLog.getComments.ended for post:{} | page:{} | size:{}", postId, page, size);

        return PageResponse.builder()
                .data(CommentMapper.INSTANCE.entityListToCommentResponseDtoList(commentPage.getContent()))
                .currentPage(commentPage.getPageable().getPageNumber() + 1)
                .totalPage(commentPage.getTotalPages())
                .build();
    }

    @Override
    public PageResponse<List<SearchPostResponse>> searchPostByCriteria(SearchPostRequest request) {
        log.info("ActionLog.searchPostByCriteria.start");

        log.debug("Creating search specification for request: {}", request);

        var spec = Specification.where(postContains(request.getPost())
                .and(categoryNameContains(request.getCategoryName()))
                .and(wallUserIdEqual(request.getWallUserId()))
                .and(excludeCategories(request.getExcludedCategories()))
                .and(creatorIdEqual(request.getCreatorId()))
                .and(checkStatus(Status.ACTIVE))
        );

        Pageable pageable = PageRequest.of(request.getPage() - 1,
                request.getPageSize(),
                Sort.by("lastUpdatedAt").descending()
        );

        var result = postEntityRepository.findAll(spec, pageable);

        List<SearchPostResponse> responses = new ArrayList<>();

        result.getContent().forEach(postEntity -> responses.add(com.uniso.equso.mapper.PostMapper
                .INSTANCE.entityToSearchPostResponse(postEntity)));

        PageResponse<List<SearchPostResponse>> pageResponse = new PageResponse<>();
        pageResponse.setData(responses);
        pageResponse.setCurrentPage(result.getPageable().getPageNumber() + 1);
        pageResponse.setTotalPage(result.getTotalPages());

        log.info("ActionLog.searchPostByCriteria.end");
        return pageResponse;
    }

    @Override
    public PostDto editPost(EditPostRequest request) {
        log.info("ActionLog.editPost.start for post:{}",request.getPostId());

        var post = postEntityRepository.findByIdAndStatus(request.getPostId(), Status.ACTIVE)
                .orElseThrow(() -> new PostException("exception.post-not-found"));

        if (!post.getCreator().getId().equals(getUserId())) {
            log.debug("User:{} has not authorized for edit post:{}",getUserId(),request.getPostId());
            throw new AuthorizationException("exception.authorization.edit-post");
        }

        log.debug("Set new text to post");
        post.setText(request.getText());

        postEntityRepository.save(post);

        log.info("ActionLog.editPost.end for post:{}", request.getPostId());
        return null;
    }


    private Long getUserId() {
        return authenticationUtil.getUserDetail().getUserEntity().getId();
    }

    private void validateAndFillWallUser(CreatePostRequest request, Long userId) {
        if (request.getWallUserId() == null) {
            request.setWallUserId(userId);
        }
    }


}
