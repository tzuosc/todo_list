package org.example.todo_list.exception.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ListError {
    // TODO 清单相关异常
//    异常描述	    错误码	触发场景
//    重复清单分类	3001	创建重复分类的清单
//    清单不存在	    3002	操作不存在清单
    ;

    private final Integer code;
    private final String message;
}
