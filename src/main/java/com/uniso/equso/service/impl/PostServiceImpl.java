package com.uniso.equso.service.impl;

import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.dao.repository.CommentEntityRepository;
import com.uniso.equso.dao.repository.PostEntityRepository;
import com.uniso.equso.dao.repository.mapper.PostMapper;
import com.uniso.equso.exceptions.AuthorizationException;
import com.uniso.equso.exceptions.PostException;
import com.uniso.equso.model.*;
import com.uniso.equso.service.PostService;
import com.uniso.equso.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final AuthenticationUtil authenticationUtil;
    private final PostEntityRepository postEntityRepository;
    private final CommentEntityRepository commentEntityRepository;

    @Override
    public void createPost(CreatePostRequest request) {
        var userId = getUserId();
        validateAndFillWallUser(request, userId);
        if (postMapper.createPost(getUserId(), request, Status.ACTIVE) <= 0) {
            log.error("POST NOT CREATED");
            throw new PostException("exception.post-not-created");
        }
    }

    @Override
    public PostDto getPost(Long postId) {
        return postMapper.getPostById(getUserId(), postId, Status.ACTIVE)
                .orElseThrow(() -> new PostException("exception.post-not-found"));
    }

    @Override
    public GetPostsResponse getPosts(GetPostsRequest criteria) {
        Integer maxCount = criteria.getMaxCount();
        boolean isZero = maxCount == 0;
        boolean hasMore = false;
        criteria.setPage((criteria.getPage() - 1) * (isZero ? 10 : criteria.getMaxCount()));
        criteria.setMaxCount(maxCount + 1);
        List<PostDto> postDtoList = postMapper.getPostsByCriteria(getUserId(), criteria, isZero, Status.ACTIVE);
        if (postDtoList.size() - 1 == maxCount && !isZero) {
            hasMore = true;
            postDtoList.remove(postDtoList.size() - 1);
        }
        return GetPostsResponse.builder()
                .posts(postDtoList)
                .hasMore(hasMore)
                .build();
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
        log.info("ActionLog.getComments.started for post:{} | page:{} | size:{}",postId,page,size);
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("lastUpdatedAt").descending());

        var commentPage = commentEntityRepository
                .findAllByPost_IdAndStatus(postId, Status.ACTIVE, pageable);

        var response = commentPage.getContent()
                .stream()
                .map(c -> CommentResponseDto.builder()
                        .id(c.getId())
                        .text(c.getText())
                        .creator(UserInfo.builder()
                                .name(c.getCreator().getName())
                                .surname(c.getCreator().getSurname())
                                .email(c.getCreator().getEmail())
                                .userType(c.getCreator().getType())
                                .subType(c.getCreator().getSubType())
                                .build())
                        .createdAt(c.getCreatedAt())
                        .build())
        .collect(Collectors.toList());

        log.info("ActionLog.getComments.ended for post:{} | page:{} | size:{}",postId,page,size);

        return PageResponse.builder()
                .data(response)
                .currentPage(commentPage.getPageable().getPageNumber()+1)
                .totalPage(commentPage.getTotalPages())
                .build();
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
