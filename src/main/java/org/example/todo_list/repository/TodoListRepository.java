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
    @Query("select (count(t) > 0) from TodoList t where t.category = ?1")
    boolean existsByCategory(String category);

    @Transactional
    @Modifying
    @Query("update TodoList t set t.category = ?1 where t.id = ?2")
    int updateCategoryById(String category, Long id);


    TodoList findByCategory(String category);
}
