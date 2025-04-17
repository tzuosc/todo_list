package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.dto.request.UpdateTaskRequest;
import org.example.todo_list.dto.response.GetTaskResponse;
import org.example.todo_list.exception.TaskException;
import org.example.todo_list.exception.errors.TaskError;
import org.example.todo_list.model.Task;
import org.example.todo_list.model.TodoList;
import org.example.todo_list.repository.TaskRepository;
import org.example.todo_list.repository.TodoListRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TodoListRepository todoListRepository;
    private final TodoListService todoListService;


    public void createTask(CreateTaskRequest createTaskRequest) {
        // 如果有重复的任务名
        if (taskRepository.existsByName(createTaskRequest.name())) {
            throw new TaskException(
                    TaskError.DUPLICATE_TASK.getCode(),
                    TaskError.DUPLICATE_TASK.getMessage());
        }
        // 如果不存在对应的任务类别
        if (!todoListRepository.existsByCategory(createTaskRequest.category())) {
            todoListService.create(createTaskRequest.category());
        }

        // 如果不是将来的时间
        if (createTaskRequest.deadline() < System.currentTimeMillis()) {
            throw new TaskException(
                    TaskError.NOT_FUTURE_TIME.getCode(),
                    TaskError.NOT_FUTURE_TIME.getMessage()
            );
        }

        TodoList list = todoListRepository.findByCategory(createTaskRequest.category());

        Task task = Task.builder()
                .name(createTaskRequest.name())
                .deadline(createTaskRequest.deadline())
                .description(createTaskRequest.taskDescription())
                .status(createTaskRequest.status())
                .todoList(list)
                .build();
        taskRepository.save(task);
        list.addTask(task);

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

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskException(
                    TaskError.TASK_NOT_FOUND.getCode(),
                    TaskError.TASK_NOT_FOUND.getMessage()
            );
        }
        taskRepository.deleteById(id);
    }


    public void updateTask(Long id, UpdateTaskRequest oldTask) {

        Optional<Task> byId = taskRepository.findById(id);
        byId.ifPresent(newTask -> {
            // 修改截至日期
            if (oldTask.deadline() != null) {
                if (oldTask.deadline() < System.currentTimeMillis()) {
                    throw new TaskException(
                            TaskError.NOT_FUTURE_TIME.getCode(),
                            TaskError.NOT_FUTURE_TIME.getMessage()
                    );
                }
                newTask.setDeadline(oldTask.deadline());
            }
            // 修改完成状态
            if (oldTask.status() != null) newTask.setStatus(oldTask.status());
            // 修改任务类别
            if (oldTask.category() != null) {
                if (!todoListRepository.existsByCategory(oldTask.category())) {
                    todoListService.create(oldTask.category());
                }
                TodoList list = todoListRepository.findByCategory(oldTask.category());
                list.addTask(newTask);
            }
            //修改任务名
            if (oldTask.name() != null) newTask.setName(oldTask.name());
            // 修改备注
            if (oldTask.description() != null) newTask.setDescription(oldTask.description());
            taskRepository.save(newTask);
        });
    }
}
