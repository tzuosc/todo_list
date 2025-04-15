package org.example.todo_list.dto.request;

import jakarta.validation.constraints.NotBlank;

/*登录或者注册的 DTO*/


public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
