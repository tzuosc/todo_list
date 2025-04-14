package org.example.todo_list.Repository;

import org.example.todo_list.Model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository<TodoList, Long> {
}
