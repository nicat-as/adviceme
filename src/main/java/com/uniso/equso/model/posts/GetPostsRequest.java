package com.uniso.equso.model.posts;

import com.uniso.equso.model.Order;
import com.uniso.equso.model.SortBy;
import lombok.Data;

@Data
public class GetPostsRequest {
    private SortBy sortBy;
    private Order orderBy;
    private String text;
    private String categoryName;
    private Long wallUserId;
    private int maxCount;
    private int page;
}
