package org.example.todo_list.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


/*
 * TODO 用于登录和注册的 DTO 请求
 *  字段说明:
 *  - username 必须, :String 类型
 *  - password 必须, :String 类型
 * */

@Schema(name = "登录请求")
@Builder
public record LoginRegisterRequest(
        @Schema(name = "username", example = "test")
        @NotBlank(message = "用户名不能为空") String username,
        @Schema(name = "password", example = "123456")
        @NotBlank(message = "密码不能为空") String password
) {
}
