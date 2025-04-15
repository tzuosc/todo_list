package org.example.todo_list.dto.request;

import jakarta.validation.constraints.NotNull;

/*登录或者注册的 DTO*/


public record LoginRequest(
        @NotNull String username,
        @NotNull String password
) {
}
