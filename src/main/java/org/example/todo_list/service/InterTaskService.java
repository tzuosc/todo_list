package org.example.todo_list.service;

import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.dto.request.UpdateTaskRequest;
import org.example.todo_list.dto.response.GetTaskResponse;

public interface InterTaskService {
    void createTask(CreateTaskRequest request);

    GetTaskResponse getTask(Long id);

    void deleteTask(Long id);

    void updateTask(Long id, UpdateTaskRequest request);
}
