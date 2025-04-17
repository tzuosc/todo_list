package org.example.todo_list.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserError {
    USER_NOT_FOUND(1001, "用户不存在"),
    INVALID_PASSWD_USER(1002, "用户名或者密码错误"), // 登陆的时候用户名或者密码错误
    DUPLICATE_USERNAME(1003, "用户名已经存在"),     // 注册的时候不能有重复的用户名
    NO_COOKIE(1004, "没有登录");

    private final Integer code;
    private final String message;
}
