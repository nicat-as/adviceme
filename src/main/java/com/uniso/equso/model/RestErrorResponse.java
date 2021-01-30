package com.uniso.equso.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestErrorResponse {
    private String uuid;
    private String code;
    private String message;
    private String status;
}
