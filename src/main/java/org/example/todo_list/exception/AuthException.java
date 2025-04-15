package org.example.todo_list.exception;

public class AuthException extends BaseException {
    public AuthException(Integer code, String message) {
        super(code, message);
    }
}
