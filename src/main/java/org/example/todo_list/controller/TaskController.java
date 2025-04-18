package org.example.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.dto.request.UpdateTaskRequest;
import org.example.todo_list.dto.response.GetTaskResponse;
import org.example.todo_list.service.TaskService;
import org.example.todo_list.utils.ApiResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "任务相关Api", description = "用于管理任务")
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "新建一个任务",
            description = "传入任务名, 类别.这两个是必须的. 还有非必须的 任务备注, 任务状态(是否完成, true 或者 false), 截至日期(时间戳)")
    @PostMapping({"/", ""})
    public ApiResponse<String> createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        taskService.createTask(createTaskRequest);
        return ApiResponse.success("创建任务成功");
    }

    @Operation(summary = "获取一个任务",
            description = "传入任务id, 返回 task的详细信息")
    @GetMapping("/{id}")
    public ApiResponse<GetTaskResponse> getTask(@PathVariable Long id) {
        return ApiResponse.success(taskService.getTask(id));
    }

    @Operation(summary = "更新任务对应的参数")
    @PatchMapping({"/{id}"})
    public ApiResponse<?> updateTask(@PathVariable Long id,
                                     @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
        taskService.updateTask(id, updateTaskRequest);
        return ApiResponse.success(null);
    }

    @Operation(summary = "删除一个任务")
    @DeleteMapping({"/{id}"})
    public ApiResponse<String> deleteTask(@NotNull @PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ApiResponse.success("删除成功");
    }
}
