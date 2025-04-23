package org.example.todo_list.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
        String username,
        String avatarUrl
) {
}
