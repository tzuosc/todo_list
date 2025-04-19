package org.example.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.response.GetListByIdResponse;
import org.example.todo_list.service.TodoListService;
import org.example.todo_list.utils.ApiResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "List相关Api", description = "用于创建, 删除, 修改, 获取任务列表")
@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
public class TodoListController {
    private final TodoListService todoListService;

    @Operation(summary = "新建一个任务列表")
    @PutMapping({"/", ""})
    public ApiResponse<String> create(String category) {
        todoListService.create(category);
        return ApiResponse.success("创建成功");
    }

    @Operation(summary = "根据id删除任务列表")
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        todoListService.delete(id);
        return ApiResponse.success("删除成功");
    }

    @Operation(summary = "根据id修改列表的类别")
    @PatchMapping("/change_category/{id}")
    public ApiResponse<String> changeCategory(@PathVariable Long id, String newCategory) {
        todoListService.changeListCategory(id, newCategory);
        return ApiResponse.success("修改成功");
    }

//    @Operation(summary = "获取所有的任务列表",
//            description = "注意, 这个api返回的值是一个json格式的数据, 每个对象包含一个 category(类别) " +
//                    "tasks(保存的是task的id),如果你你想获取对应的id, 那么你需要调用task的api -> /task/get 逐个获取task")
//    @GetMapping({"/", ""})
//    public ApiResponse<List<GetAllListResponse>> fetch() {
//        List<GetAllListResponse> allLists = todoListService.getAllLists();
//        return ApiResponse.success(allLists);
//    }

    @Operation(summary = "根据id获取任务列表")
    @GetMapping("/{id}")
    public ApiResponse<GetListByIdResponse> get(@PathVariable Long id) {
        GetListByIdResponse listById = todoListService.getListById(id);
        return ApiResponse.success(listById);
    }
}
