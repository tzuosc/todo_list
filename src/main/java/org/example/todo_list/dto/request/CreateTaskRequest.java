package org.example.todo_list.dto.request;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record CreateTaskRequest(
        @NotBlank(message = "任务类别不能为空") String category,
        @NotBlank(message = "任务名不能为空") String name,
        @NotNull @AssertFalse(message = "传入的时候必须是 False") Boolean status,
        String taskDescription,
        @Future(message = "截至日期必须是未来的某个时间") Date deadline
) {
}

