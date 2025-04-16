package org.example.todo_list.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GetAllListResponse(
        @NotNull String category,
        List<Long> tasks
) {
}
