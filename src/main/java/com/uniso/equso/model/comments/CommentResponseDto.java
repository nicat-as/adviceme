package com.uniso.equso.model.comments;

import com.uniso.equso.model.users.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private UserInfo creator;
}
