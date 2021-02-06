package com.uniso.equso.model.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchPostRequest {
    private String post;
    private String categoryName;
    private Long creatorId;
    private Long wallUserId;
    private Integer page = 1;
    private Integer pageSize = 10;
}
