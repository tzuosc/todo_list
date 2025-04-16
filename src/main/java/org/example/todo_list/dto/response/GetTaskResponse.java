package org.example.todo_list.dto.response;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record GetTaskResponse(
        @NotNull Long id,
        @NotNull LocalDateTime deadline,
        @NotNull String name,
        @NotNull String description,
        @NotNull boolean status
) {
}
