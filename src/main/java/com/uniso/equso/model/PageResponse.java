package com.uniso.equso.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T>{
    private T data;
    private int currentPage;
    private int totalPage;

}
