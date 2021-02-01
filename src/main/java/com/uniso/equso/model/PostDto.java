package com.uniso.equso.model;

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
public class PostDto {
    private Long id;
    private String text;
    private UserInfoDto creator;
    private UserInfoDto wallUser;
    private PostCategoryDto category;
    private LocalDateTime createdAt;
}
