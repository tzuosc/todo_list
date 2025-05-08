package org.example.todo_list.exception.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ListError {
    // TODO 任务列表异常枚举
    ;

    private final Integer code;
    private final String message;
}
