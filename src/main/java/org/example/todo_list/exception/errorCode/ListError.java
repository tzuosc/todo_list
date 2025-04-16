package org.example.todo_list.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ListError {
    ;

    private final Integer code;
    private final String message;
}
