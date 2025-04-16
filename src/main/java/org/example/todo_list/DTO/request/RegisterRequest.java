package org.example.todo_list.dto.request;

import jakarta.validation.constraints.NotBlank;


// 用于注册的 DTO 其中头像的url可以为空

public record RegisterRequest(
        @NotBlank(message = "用户名不能为空") String username,
        @NotBlank(message = "密码不能为空") String password,
        String avatarUrl
) {
}
