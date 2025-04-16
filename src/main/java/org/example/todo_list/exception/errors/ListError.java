package org.example.todo_list.exception.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ListError {
    DUPLICATE_LIST(1006, "重复类型的任务"),
    LIST_NOT_FOUND(1007, "没找到任务列表");

    private final Integer code;
    private final String message;
}
