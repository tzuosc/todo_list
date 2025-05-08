package org.example.todo_list.repository.JPA;

import org.example.todo_list.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {

}
