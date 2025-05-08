package org.example.todo_list.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/*
 * TODO 获取任务的 DTO 响应
 *  字段说明:
 *   - id :Long
 *   - deadline :Long
 *   - name :String
 *   - description :String
 *   - status :boolean
 *   所有字段都是必须的
 * */
@Schema(name = "获取任务的响应")
@Builder
public record GetTaskResponse(
        @NotNull Long id,
        Long deadline,
        @NotNull String name,
        @Nullable String description,
        @NotNull boolean status
) {
}
