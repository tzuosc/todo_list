package org.example.todo_list.dto.request;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record CreateTaskRequest(
        @NotBlank String category,
        @NotBlank String taskName,
        String taskDescription,
        @AssertFalse(message = "传入的时候必须是 False") Boolean status,
        @Future(message = "截至日期必须是未来的某个时间") Date deadline
) {
}

