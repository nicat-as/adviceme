package com.uniso.equso.model.users;

import com.uniso.equso.annotation.WeakPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    private String currentPassword;
    @WeakPassword
    private String newPassword;
}
