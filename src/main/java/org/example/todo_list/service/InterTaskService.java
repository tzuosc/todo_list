package org.example.todo_list.service;

import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.dto.request.UpdateTaskRequest;
import org.example.todo_list.dto.response.GetTaskResponse;

public interface InterTaskService {
    public void createTask(CreateTaskRequest request);

    public GetTaskResponse getTask(Long id);

    public void deleteTask(Long id);

    public void updateTask(Long id, UpdateTaskRequest request);
}
