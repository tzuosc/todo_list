package org.example.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
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

import java.util.Date;

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


    @GetMapping("/{id}")
    public ApiResponse<GetTaskResponse> getTask(@Min(value = 1, message = "id至少为1")
                                                @PathVariable Long id) {
        return ApiResponse.success(taskService.getTask(id));
    }

    @PatchMapping("/finish")
    public ApiResponse<String> finishTask(@NotNull @RequestParam Long id) {
        taskService.changeStatus(id, true);
        return ApiResponse.success("任务完成");
    }

    @PatchMapping("/not_finish")
    public ApiResponse<String> notFinishTask(@NotNull @RequestParam Long id) {
        taskService.changeStatus(id, false);
        return ApiResponse.success("成功修改任务状态");
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteTask(@NotNull @RequestParam Long id) {
        taskService.deleteTask(id);
        return ApiResponse.success("删除成功");
    }

    @PatchMapping("/change_deadline")
    public ApiResponse<String> changeDeadline(@NotNull @RequestParam Long id, @NotNull Date deadline) {
        taskService.changeDeadline(id, deadline);
        return ApiResponse.success("成功修改截至日期");
    }
}
