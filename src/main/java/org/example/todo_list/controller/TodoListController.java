package org.example.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.response.GetListResponse;
import org.example.todo_list.security.JwtUtils;
import org.example.todo_list.service.TodoListService;
import org.example.todo_list.utils.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "List相关Api", description = "用于创建, 删除, 修改, 获取任务列表")
@RestController
@RequestMapping("/list")
@RequiredArgsConstructor// 通过 Lombok 自动生成包含所有 final 字段的构造函数，简化代码书写。
public class TodoListController {

//private final TodoListService todoListService;

//    @Operation(summary = "新建一个任务列表")
//    @PutMapping("/{category}")
//    public ApiResponse<String> create(@PathVariable String category, @RequestAttribute Long userId) {
//        // TODO 新建一个任务列表, 传入一个 category,返回"创建成功"字符串
//    }

//    @Operation(summary = "根据id删除任务列表")
//    @DeleteMapping("/{id}")
//    public ApiResponse<String> delete(@PathVariable Long id, @RequestAttribute Long userId) {
//        // TODO 删除任务列表, 传入一个 id,返回"删除成功"字符串

//    }

//    @Operation(summary = "根据id修改列表的类别")
//    @PatchMapping("/change_category/{id}")
//    public ApiResponse<String> changeCategory(@PathVariable Long id, @RequestAttribute Long userId,
//                                              @Valid @RequestParam @NotBlank(message = "类别不能为空") String newCategory) {
//        // TODO 修改任务列表的类型. 传入一个 id, 一个 newCategory,一个用户id,返回"修改成功"字符串
//
//    }

//    @Operation(summary = "获取所有的任务列表",
//            description = "注意, 这个api返回的值是一个json格式的数据, 每个对象包含一个 category(类别) " +
//                    "tasks(保存的是task的id),如果你你想获取对应的id, 那么你需要调用task的api -> /task/get 逐个获取task")
//    @GetMapping({"/", ""})
//    public ApiResponse<List<GetListResponse>> fetch(@RequestAttribute Long userId) {
//        // TODO 获取所有的任务列表, 什么都不用传入,返回一个表示操作成功的API响应，封装了获取到的所有任务列表数据 allLists
//    }

//    @Operation(summary = "根据id获取任务列表")
//    @GetMapping("/{id}")
//    public ApiResponse<GetListResponse> get(@PathVariable Long id) {
//        // TODO 根据 id 获取对应的任务列表,返回一个封装了任务列表查询结果的响应对象
//    }
}
