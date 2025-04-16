package org.example.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.dto.response.GetTaskResponse;
import org.example.todo_list.service.TaskService;
import org.example.todo_list.utils.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "任务相关Api", description = "用于管理任务")
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "新建一个任务",
            description = "传入任务名, 类别.这两个是必须的. 还有非必须的 任务备注, 任务状态(是否完成), 截至日期")
    @PostMapping("/create")
    public ApiResponse<String> createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        taskService.createTask(createTaskRequest);
        return ApiResponse.success("创建任务成功");
    }

    @Operation(summary = "获取一个任务",
            description = "传入任务id, 返回 task的详细信息")
    @GetMapping("/{id}")
    public ApiResponse<GetTaskResponse> getTask(@Min(value = 1, message = "id至少为1")
                                                @PathVariable Long id) {
        return ApiResponse.success(taskService.getTask(id));
    }

    @Operation(summary = "把一个任务标记为已完成")
    @PatchMapping("/finish")
    public ApiResponse<String> finishTask(@NotNull @RequestParam Long id) {
        taskService.changeStatus(id, true);
        return ApiResponse.success("任务完成");
    }

    @Operation(summary = "把已经完成的任务标记为未完成")
    @PatchMapping("/not_finish")
    public ApiResponse<String> notFinishTask(@NotNull @RequestParam Long id) {
        taskService.changeStatus(id, false);
        return ApiResponse.success("成功修改任务状态");
    }

    @Operation(summary = "删除一个任务")
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteTask(@NotNull @RequestParam Long id) {
        taskService.deleteTask(id);
        return ApiResponse.success("删除成功");
    }

    @Operation(summary = "修改截止日期")
    @PatchMapping("/change_deadline")
    public ApiResponse<String> changeDeadline(@NotNull @RequestParam Long id, @NotNull LocalDateTime deadline) {
        taskService.changeDeadline(id, deadline);
        return ApiResponse.success("成功修改截至日期");
    }
}
