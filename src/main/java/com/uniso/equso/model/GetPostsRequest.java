package com.uniso.equso.model;

import lombok.Data;

@Data
public class GetPostsRequest {
//    private SortBy sortBy;
//    private Order orderBy;
    private String text;
    private String categoryName;
    private Long wallUserId;
    private int maxCount;
    private int page;
}
