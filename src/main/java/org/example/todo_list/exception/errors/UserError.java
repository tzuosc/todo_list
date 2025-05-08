package org.example.todo_list.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserError {
    // 注册的时候使用了非法字符
    INVALID_USERNAME(1001, "非法用户名"),
    // 登陆的时候用户名或者密码错误
    INVALID_PASSWD_USER(1002, "用户名或者密码错误"),
    // 注册的时候不能有重复的用户名
    DUPLICATE_USERNAME(1003, "用户名已经存在"),
    NO_COOKIE(1004, "没有登录"),
    NO_USER(1005, "没找到用户"),
    INVALID_FILE(1007, "非法文件类型"),
    EMPTY_FILE(1006, "文件为空");

    private final Integer code;
    private final String message;
}
