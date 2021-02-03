package com.uniso.equso.model.posts;

import com.uniso.equso.model.categories.CategoryDto;
import com.uniso.equso.model.comments.CommentResponseDto;
import com.uniso.equso.model.users.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchPostResponse {
    private Long id;
    private String text;
    private UserInfo creator;
    private UserInfo wallUser;
    private CategoryDto category;
    private List<CommentResponseDto> comments;
    private LocalDateTime createdAt;
}
