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
import org.example.todo_list.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TodoListRepository todoListRepository;
    private final TodoListService todoListService;
    private final UserRepository userRepository;


    public void createTask(CreateTaskRequest createTaskRequest, Long userId) {
        /*
         * TODO 新建任务:
         *   你需要处理的业务异常:
         *   - 如果不存在对应的任务类别
         *   - 如果截至日期不是将来的时间
         *   - 如果截至日期超过了 2038 年, 也就是 2147483647L(时间戳表示的最大数)
         * */

        // 如果不存在对应的任务类别
        TodoList list = todoListRepository.findByCategory(createTaskRequest.category(), userId);
        if (list == null) {
            todoListService.create(createTaskRequest.category(), userId);
            TodoList newList = todoListRepository.findByCategory(createTaskRequest.category(), userId);

            userRepository.findById(userId).ifPresent(user -> {
                user.addTodoList(newList);
            });
        }

        if (createTaskRequest.status() == null) {
            throw new TaskException(
                    TaskError.INVALID_STATUS.getCode(),
                    TaskError.INVALID_STATUS.getMessage()
            );
        }

        if (createTaskRequest.deadline() != null) {
            // 如果不是将来的时间
            if (createTaskRequest.deadline() < System.currentTimeMillis() / 1000) {
                throw new TaskException(
                        TaskError.NOT_FUTURE_TIME.getCode(),
                        TaskError.NOT_FUTURE_TIME.getMessage()
                );
            }
            if (createTaskRequest.deadline() > 2147483647L) {
                throw new TaskException(
                        TaskError.INVALID_TIME.getCode(),
                        TaskError.INVALID_TIME.getMessage()
                );
            }

        }

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
        /*
         * TODO 获取任务: 你需要处理如果 id 对应的 task 不存在
         * */

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
        /*
         * TODO 删除任务: 你需要处理如果 id 对应的 task 不存在
         * */
        if (!taskRepository.existsById(id)) {
            throw new TaskException(
                    TaskError.TASK_NOT_FOUND.getCode(),
                    TaskError.TASK_NOT_FOUND.getMessage()
            );
        }
        taskRepository.deleteById(id);
    }


    public void updateTask(Long id, UpdateTaskRequest oldTask, Long userId) {

        /*
         * TODO 更新任务
         *   你需要处理的业务异常:
         *   - 如果有截至日期: 新截至日期超过了 2038 年, 新的截止日期不是将来的时间
         *   - 如果有类别: 如果没有对应的类别, 你需要新建一个对应的类别的 todoList
         *   - id 对应的 task 不存在
         * */

        Optional<Task> byId = taskRepository.findById(id);
        byId.ifPresentOrElse(newTask -> {
                    // 修改截至日期
                    if (oldTask.deadline() != null) {
                        if (oldTask.deadline() < System.currentTimeMillis() / 1000) {
                            throw new TaskException(
                                    TaskError.NOT_FUTURE_TIME.getCode(),
                                    TaskError.NOT_FUTURE_TIME.getMessage()
                            );
                        }
                        if (oldTask.deadline() > 2147483647L) {
                            throw new TaskException(
                                    TaskError.INVALID_TIME.getCode(),
                                    TaskError.INVALID_TIME.getMessage()
                            );
                        }
                        newTask.setDeadline(oldTask.deadline());
                    }
                    // 修改完成状态
                    if (oldTask.status() != null) newTask.setStatus(oldTask.status());
                    // 修改任务类别
                    if (oldTask.category() != null) {
                        if (!todoListRepository.existsByCategory(oldTask.category(), userId)) {
                            todoListService.create(oldTask.category(), userId);
                        }
                        TodoList list = todoListRepository.findByCategory(oldTask.category(), userId);
                        list.addTask(newTask);
                    }
                    //修改任务名
                    if (oldTask.name() != null) newTask.setName(oldTask.name());
                    // 修改备注
                    if (oldTask.description() != null) newTask.setDescription(oldTask.description());
                    taskRepository.save(newTask);
                },
                () -> {
                    throw new TaskException(
                            TaskError.TASK_NOT_FOUND.getCode(),
                            TaskError.TASK_NOT_FOUND.getMessage()
                    );
                }
        );
    }
}
