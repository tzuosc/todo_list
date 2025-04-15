package org.example.todo_list.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskError {
    DUPLICATE_TASK(1004, "重复的任务");


    private final Integer code;
    private final String message;
}
