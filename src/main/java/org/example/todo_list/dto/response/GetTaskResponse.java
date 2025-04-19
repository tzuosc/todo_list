package org.example.todo_list.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(name = "获取任务的响应")
@Builder
public record GetTaskResponse(
        @NotNull Long id,
        Long deadline,
        @NotNull String name,
        @NotNull String description,
        @NotNull boolean status
) {
}
