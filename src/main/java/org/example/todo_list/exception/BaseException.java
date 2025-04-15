package org.example.todo_list.exception;

import lombok.Getter;
import lombok.Setter;

//@RequiredArgsConstructor
@Getter
@Setter
public abstract class BaseException extends RuntimeException {
    private final String message;
    private final Integer code;

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
