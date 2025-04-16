package org.example.todo_list.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record GetTaskResponse(
        @NotNull Long id,
        @NotNull Date deadline,
        @NotNull String name,
        @NotNull String description,
        @NotNull boolean status
) {
}
