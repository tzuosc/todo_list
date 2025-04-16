package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.dto.response.GetTaskResponse;
import org.example.todo_list.exception.TaskException;
import org.example.todo_list.exception.errors.TaskError;
import org.example.todo_list.model.Task;
import org.example.todo_list.repository.TaskRepository;
import org.example.todo_list.repository.TodoListRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TodoListRepository todoListRepository;


    public void createTask(CreateTaskRequest createTaskRequest) {
        // 如果有重复的任务名
        if (taskRepository.existsByName(createTaskRequest.name())) {
            throw new TaskException(
                    TaskError.DUPLICATE_TASK.getCode(),
                    TaskError.DUPLICATE_TASK.getMessage());
        }

        Task task = Task.builder()
                .name(createTaskRequest.name())
                .deadline(createTaskRequest.deadline())
                .description(createTaskRequest.taskDescription())
                .status(createTaskRequest.status())
                .todoList(todoListRepository.findByCategory(createTaskRequest.category()))
                .build();
        taskRepository.save(task);


    }

    public GetTaskResponse getTask(Long id) {
        return taskRepository.findById(id)
                .map(task ->
                        new GetTaskResponse(task.getId(),
                                task.getDeadline(),
                                task.getName(),
                                task.getDescription(),
                                task.isStatus()))
                .orElseThrow(() ->
                        new TaskException(
                                TaskError.TASK_NOT_FOUND.getCode(),
                                TaskError.TASK_NOT_FOUND.getMessage()
                        ));
    }

    public void changeStatus(Long id, boolean status) {

        int res = taskRepository.updateStatusById(status, id);
        if (res == 0)
            throw new TaskException(
                    TaskError.TASK_NOT_FOUND.getCode(),
                    TaskError.TASK_NOT_FOUND.getMessage()
            );
    }


    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskException(
                    TaskError.TASK_NOT_FOUND.getCode(),
                    TaskError.TASK_NOT_FOUND.getMessage()
            );
        }
        taskRepository.deleteById(id);
    }


    public void changeDeadline(Long id, Date deadline) {

        int res = taskRepository.updateTaskDateById(deadline, id);
        if (res == 0)
            throw new TaskException(
                    TaskError.TASK_NOT_FOUND.getCode(),
                    TaskError.TASK_NOT_FOUND.getMessage()
            );
    }
}
