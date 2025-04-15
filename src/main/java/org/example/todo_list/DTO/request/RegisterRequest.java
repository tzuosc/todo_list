package org.example.todo_list.DTO.request;

import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotNull String username,
        @NotNull String password,
        @NotNull String avatarUrl
) {
}
