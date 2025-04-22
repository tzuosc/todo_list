package org.example.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/*
 * TODO 用于更新任务的 DTO 请求
 *  字段说明:
 *   - category :String
 *   - name :String
 *   - status :boolean
 *   - description :String
 *   - deadline :Long
 *   所有字段都是非必须的.
 * */

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
        Long deadline
) {
}
