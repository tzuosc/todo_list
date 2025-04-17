package org.example.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "新建任务请求")
public record CreateTaskRequest(
        @Schema(name = "category", example = "work")
        @NotBlank(message = "任务类别不能为空") String category,


        @Schema(name = "name", example = "task1")
        @NotBlank(message = "任务名不能为空") String name,

        @Schema(name = "status", example = "false")
        @NotNull @AssertFalse(message = "传入的时候必须是 False") Boolean status,

        @Schema(name = "description", example = "this is a task")
        String taskDescription,

        Long deadline
) {
}

