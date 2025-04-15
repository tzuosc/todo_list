package org.example.todo_list.controller;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.service.TaskService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

//    @PostMapping("/create")
//    public ApiResponse<String> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
//
//    }
}
