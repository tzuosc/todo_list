package org.example.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


// 用于注册的 DTO 其中头像的url可以为空
@Builder
@Schema(name = "注册请求")
public record RegisterRequest(
        @Schema(name = "username", example = "test")
        @NotBlank(message = "用户名不能为空") String username,

        @Schema(name = "password", example = "123456")
        @NotBlank(message = "密码不能为空") String password,

        @Schema(name = "avatarUrl", example = "xxxx")
        String avatarUrl
) {
}
