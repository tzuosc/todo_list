package org.example.todo_list.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.response.GetListResponse;
import org.example.todo_list.exception.ListException;
import org.example.todo_list.exception.UserException;
import org.example.todo_list.exception.errors.ListError;
import org.example.todo_list.exception.errors.UserError;
import org.example.todo_list.model.Task;
import org.example.todo_list.model.TodoList;
import org.example.todo_list.repository.TaskRepository;
import org.example.todo_list.repository.TodoListRepository;
import org.example.todo_list.repository.UserRepository;
import org.example.todo_list.security.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TodoListService {
    private final TodoListRepository todoListRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public void create(String category, Long userId) {
        /*
         * TODO 新建任务列表: 如果你没有对应的类别的 todolist 你需要新建一个 todolist
         * */
        if (todoListRepository.existsByCategory(category, userId)) {
            throw new ListException(
                    ListError.DUPLICATE_CATEGORY.getCode(),
                    ListError.DUPLICATE_CATEGORY.getMessage()
            );
        }

        userRepository.findById(userId).ifPresentOrElse(
                user -> {
                    TodoList todoList = TodoList.builder()
                            .category(category)
                            .tasks(new ArrayList<>())
                            .user(user)
                            .build();
                    todoListRepository.save(todoList);
                    user.addTodoList(todoList);
                },
                () -> {
                    throw new UserException(
                            UserError.NO_USER.getCode(),
                            UserError.NO_USER.getMessage()
                    );
                }
        );

    }

    public void delete(Long id, Long userId) {
        // TODO 删除 todolist 如果你在模型没有设置级联删除, 那么你需要在这里一个个删除所有 task. 你需要处理 id 对应的 todolist 不存在的情况
        todoListRepository.findById(id).ifPresentOrElse(
                todoList ->
                {
                    todoListRepository.deleteById(id);
                    userRepository.findById(userId).ifPresentOrElse(
                            user -> user.removeTodoList(todoList),
                            () -> {
                                throw new UserException(
                                        UserError.NO_USER.getCode(),
                                        UserError.NO_USER.getMessage()
                                );
                            }
                    );
                },
                () -> {
                    throw new ListException(
                            ListError.LIST_NOT_FOUND.getCode(),
                            ListError.LIST_NOT_FOUND.getMessage()
                    );
                }
        );


    }

    public void changeListCategory(Long id, String newCategory, Long userId) {

        /*
         * TODO 更新 todolist 的类别
         *   你需要处理:
         *   - 类别已存在
         *   - id 对应的 todolist 不存在
         * */
        if (todoListRepository.existsByCategory(newCategory, userId)) {
            throw new ListException(
                    ListError.DUPLICATE_CATEGORY.getCode(),
                    ListError.DUPLICATE_CATEGORY.getMessage()
            );
        }

        int res = todoListRepository.updateCategoryById(newCategory, id);
        if (res != 1) {
            throw new ListException(
                    ListError.LIST_NOT_FOUND.getCode(),
                    ListError.LIST_NOT_FOUND.getMessage()
            );
        }
    }


    public List<GetListResponse> getAllLists(Long userId) {
        /*
         * TODO 获取所有 todolist
         *   循环获取所有 list, 为此, 我准备了 taskRepository.findTaskIdsByCategory
         * */
        List<TodoList> todoListByUserId = todoListRepository.findTodoListByUserId(userId);
        List<GetListResponse> res = new ArrayList<>();
        todoListByUserId.forEach(todoList -> {
            res.add(GetListResponse.builder()
                    .tasks(todoList.getTasks().stream().map(Task::getId).toList())
                    .id(todoList.getId())
                    .category(todoList.getCategory())
                    .build());
        });
        return res;
    }

    public GetListResponse getListById(Long id) {

        // TODO 根据 id 获取 todolist, 你只需要判断 id 对应的 todolist 是否存在就行了

        Optional<TodoList> listById = todoListRepository.findById(id);
        if (listById.isPresent()) {
            List<Long> allByTodoListId = taskRepository.findIdsByTodoList_Id(id);
            return GetListResponse.builder()
                    .id(id).
                    category(listById.get().getCategory())
                    .tasks(allByTodoListId).build();
        } else {
            throw new ListException(
                    ListError.LIST_NOT_FOUND.getCode(),
                    ListError.LIST_NOT_FOUND.getMessage()
            );
        }
    }
}

