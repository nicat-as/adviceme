package com.uniso.equso.model.posts;

import lombok.Data;


@Data
public class CreatePostRequest {
    private String text;
    private Long wallUserId;
    private Long categoryId;
}
