package org.example.todo_list.controller;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.response.GetAllListResponse;
import org.example.todo_list.service.TodoListService;
import org.example.todo_list.utils.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
//@Validated
public class TodoListController {
    private final TodoListService todoListService;

    @PutMapping("/create")
    public void create(String category) {
        todoListService.create(category);
    }

    @DeleteMapping("/delete")
    public void delete(Long id) {
        todoListService.delete(id);
    }

    @PatchMapping("/change_category")
    public void changeCategory(Long id, String newCategory) {
        todoListService.changeListCategory(id, newCategory);
    }

    @GetMapping("/fetch")
    public ApiResponse<List<GetAllListResponse>> fetch() {
        List<GetAllListResponse> allLists = todoListService.getAllLists();
        return ApiResponse.success(allLists);
    }

}
