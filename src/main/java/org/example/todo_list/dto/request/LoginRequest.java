package org.example.todo_list.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/*登录或者注册的 DTO*/


public record LoginRequest(
        @NotBlank(message = "用户名不能为空") String username,
        @NotNull(message = "缺少密码字段") @NotBlank(message = "密码不能为空") String password
) {
}
