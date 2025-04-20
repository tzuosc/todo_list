package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.response.GetListResponse;
import org.example.todo_list.exception.ListException;
import org.example.todo_list.exception.errors.ListError;
import org.example.todo_list.model.TodoList;
import org.example.todo_list.repository.TaskRepository;
import org.example.todo_list.repository.TodoListRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TodoListService implements InterTodoListService {
    private final TodoListRepository todoListRepository;
    private final TaskRepository taskRepository;

    public void create(String category) {
        if (todoListRepository.existsByCategory(category)) {
            throw new ListException(
                    ListError.DUPLICATE_LIST.getCode(),
                    ListError.DUPLICATE_LIST.getMessage()
            );
        }

        TodoList todoList = TodoList.builder()
                .category(category)
                .tasks(new ArrayList<>())
                .build();
        todoListRepository.save(todoList);
//        todoList.addTask(new Task());
    }

    public void delete(Long id) {
        if (!todoListRepository.existsById(id)) {
            throw new ListException(
                    ListError.LIST_NOT_FOUND.getCode(),
                    ListError.LIST_NOT_FOUND.getMessage()
            );
        }
        todoListRepository.deleteById(id);
    }

    public void changeListCategory(Long id, String newCategory) {
        if (todoListRepository.existsByCategory(newCategory)) {
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


    public List<GetListResponse> getAllLists() {
        List<TodoList> allList = todoListRepository.findAll();
        List<GetListResponse> responses = new ArrayList<>();
        allList.forEach(todoList -> {
            List<Long> tasks = taskRepository.findTaskIdsByCategory(todoList.getCategory());
            responses.add(new GetListResponse(todoList.getId(), todoList.getCategory(), tasks));
        });
        return responses;
    }

    public GetListResponse getListById(Long id) {
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

