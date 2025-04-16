package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.repository.TaskRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TodoListService {
    private final TaskRepository taskRepository;

//    public void create(String category) {
//
//    }
}
