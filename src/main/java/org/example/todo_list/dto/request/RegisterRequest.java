package org.example.todo_list.dto.request;

import jakarta.validation.constraints.NotNull;

// 用于注册的 DTO 其中头像的url可以为空

public record RegisterRequest(
        @NotNull String username,
        @NotNull String password,
        String avatarUrl
) {
}
