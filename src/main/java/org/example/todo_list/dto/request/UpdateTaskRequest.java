package org.example.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.Builder;

@Schema
@Builder
public record UpdateTaskRequest(

        @Schema(name = "category", example = "work")
        String category,

        @Schema(name = "name", example = "task1")
        String name,

        @Schema(name = "status", example = "false")
        Boolean status,

        @Schema(name = "description", example = "this is a task")
        String description,

        @Schema(name = "deadline", example = "2177423998")
        @Max(value = 2177423999L, message = "非法日期")
        Long deadline
) {
}
