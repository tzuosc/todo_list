package org.example.todo_list.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskError {
    ;

    private final Integer code;
    private final String message;
}
