package com.uniso.equso.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
