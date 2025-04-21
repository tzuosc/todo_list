package org.example.todo_list.repository;

import org.example.todo_list.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    @Transactional
    @Modifying
    @Query("update TodoList t set t.category = ?1 where t.id = ?2")
    int updateCategoryById(String category, Long id);


    boolean existsByCategory(String category);

    TodoList findByCategory(String category);
}
