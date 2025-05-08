package org.example.todo_list.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

/*
 * TODO 获取所有 list 的 DTO 响应
 *  字段说明:
 *  - id :Long
 *  - category :String
 *  - tasks :List<Long> 存储所有 task 的 id. 这是扁平化的 DTO 设计, 我并不会使用一个列表直接存储所有的 task 对应的实体类, 而是存储它的 id. 这符合最佳实践
 *  所有字段都是必须的
 * */

@Schema(name = "获取所有list的响应")
@Builder
public record GetListResponse(
        @NotNull Long id,
        @NotNull String category,
        List<Long> tasks
) {
}
