package org.example.todo_list.repository;

import org.example.todo_list.model.TodoList;
import org.example.todo_list.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {

}
