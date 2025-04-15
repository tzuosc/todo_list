package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

//    public boolean createTask(CreateTaskRequest createTaskRequest) {
//        Task task = Task.builder()
//                .taskName(createTaskRequest.taskName())
//                .status(createTaskRequest.status())
//                .taskDate(createTaskRequest.deadline())
//                .taskDescription(createTaskRequest.taskDescription())
//                .build();
//    }
}
