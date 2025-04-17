package org.example.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record UpdateTaskRequest(

        @Schema(name = "category", example = "work")
        String category,

        @Schema(name = "name", example = "task1")
        String name,

        @Schema(name = "status", example = "false")
        Boolean status,

        @Schema(name = "description", example = "this is a task")
        String description,

        Long deadline
) {
}
