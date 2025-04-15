package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.model.Task;
import org.example.todo_list.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public boolean createTask(CreateTaskRequest createTaskRequest) {
        Task task = Task.builder()
                .name(createTaskRequest.name())
                .taskDate(createTaskRequest.deadline())
                .description(createTaskRequest.taskDescription())
                .status(createTaskRequest.status())
                .build();
        taskRepository.save(task);
        return true;
    }
}
