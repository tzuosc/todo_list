package org.example.todo_list.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskError {
    INVALID_TIME(2001, "非法时间"),
    TASK_NOT_FOUND(2002, "没找到任务"),
    NOT_FUTURE_TIME(2003, "不是将来的时间"),
    INVALID_STATUS(2004, "错误的状态");

    private final Integer code;
    private final String message;
}
