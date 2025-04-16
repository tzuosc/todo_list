package org.example.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/*登录或者注册的 DTO*/

@Schema(name = "登录请求")
public record LoginRequest(
        @Schema(name = "username", example = "test")
        @NotBlank(message = "用户名不能为空") String username,
        
        @Schema(name = "password", example = "123456")
        @NotNull(message = "缺少密码字段") @NotBlank(message = "密码不能为空") String password
) {
}
