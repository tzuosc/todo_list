package org.example.todo_list.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserError {
    // 注册的时候使用了非法字符
    INVALID_USERNAME(1001, "非法用户名"),
    // 注册的时候不能有重复的用户名
    DUPLICATE_USERNAME(1003, "用户名已经存在"),
    NO_COOKIE(1004, "没有登录");
    // TODO 用户异常枚举. 登陆的时候用户名或者密码错误等等 ...

    private final Integer code;
    private final String message;
}
