package org.example.todo_list.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListException extends BaseException {
    public ListException(Integer code, String message) {
        super(code, message, "ListError");
    }
}
