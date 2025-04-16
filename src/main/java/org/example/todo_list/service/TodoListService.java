package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.response.GetAllListResponse;
import org.example.todo_list.exception.ListException;
import org.example.todo_list.exception.errors.ListError;
import org.example.todo_list.model.TodoList;
import org.example.todo_list.repository.TaskRepository;
import org.example.todo_list.repository.TodoListRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TodoListService {
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
        int res = todoListRepository.updateCategoryById(newCategory, id);
        if (res != 1) {
            throw new ListException(
                    ListError.LIST_NOT_FOUND.getCode(),
                    ListError.LIST_NOT_FOUND.getMessage()
            );
        }
    }

    public List<GetAllListResponse> getAllLists() {

        List<String> allList = todoListRepository.findAllCategories();
        List<GetAllListResponse> responses = new ArrayList<>();
        allList.forEach(category -> {
            List<Long> tasks = taskRepository.findTaskIdsByCategory(category);
            responses.add(new GetAllListResponse(category, tasks));
        });
        return responses;
    }
}
