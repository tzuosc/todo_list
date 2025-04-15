package org.example.todo_list.controller;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.service.TaskService;
import org.example.todo_list.utils.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ApiResponse<String> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        taskService.createTask(createTaskRequest);
        return ApiResponse.success("创建任务成功");
    }
}
