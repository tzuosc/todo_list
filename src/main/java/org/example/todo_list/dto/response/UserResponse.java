package org.example.todo_list.dto.response;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.Builder;

/*
 * TODO User 的响应 DTO
 *  字段说明:
 *  - username 必须, :String 类型
 *  - password 必须, :String 类型
 * */
@Builder
public record UserResponse(
        Long id,
        String username,
        @Nullable String avatarUrl
) {
}
