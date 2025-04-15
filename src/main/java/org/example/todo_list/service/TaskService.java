package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.exception.TaskError;
import org.example.todo_list.exception.TaskException;
import org.example.todo_list.model.Task;
import org.example.todo_list.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public void createTask(CreateTaskRequest createTaskRequest) {
        // 如果有重复的任务名
        if (taskRepository.existsByName(createTaskRequest.name())) {
            throw new TaskException(
                    TaskError.DUPLICATE_TASK.getCode(),
                    TaskError.DUPLICATE_TASK.getMessage());
        }

        Task task = Task.builder()
                .name(createTaskRequest.name())
                .taskDate(createTaskRequest.deadline())
                .description(createTaskRequest.taskDescription())
                .status(createTaskRequest.status())
                .build();
        taskRepository.save(task);

    }
}
