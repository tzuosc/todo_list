package org.example.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/*
 * TODO 用于新建任务的 DTO 请求
 *  字段说明:
 *  - category 必须, :String 类型
 *  - name 必须, :String 类型
 *  - status 必须, 传入必须为 false, :boolean 类型
 *  - taskDescription 不是必须的 :String 类型
 *  - deadline 不是必须的 :Long 类型, 用时间戳表示
 * */

@Builder
@Schema(name = "新建任务请求")
public record CreateTaskRequest(


        @Schema(name = "category", example = "work")
        @NotBlank(message = "任务类别不能为空") String category,

        @Schema(name = "name", example = "task")
        @NotBlank(message = "任务名不能为空") String name,

        @Schema(name = "status", example = "false")
        @NotNull @AssertFalse(message = "完成状态传入的时候必须是 False") Boolean status,

        @Schema(name = "description", example = "this is a task")
        String taskDescription,

        @Schema(name = "deadline", example = "1830268799")
        Long deadline
) {
}

