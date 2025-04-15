package org.example.todo_list.controller;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.service.TaskService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
public class TodoListController {
    private final TaskService taskService;
}
