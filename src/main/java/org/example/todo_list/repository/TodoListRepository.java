package org.example.todo_list.repository;

import org.example.todo_list.model.TodoList;
import org.example.todo_list.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    @Transactional
    @Modifying
    @Query("update TodoList t set t.category = ?1 where t.id = ?2")
    int updateCategoryById(String category, Long id);

    @jakarta.transaction.Transactional
    @Query("select t from TodoList t left join fetch t.tasks where t.user.id= ?1")
    List<TodoList> findTodoListByUserId(Long userId);

    @Transactional
    @Query("select count(t)>0 from TodoList t  where t.user.id = ?2 and t.category=?1")
    boolean existsByCategory(String category, Long UserId);

    @Transactional
    @Query("select t from TodoList t  where t.user.id = ?2 and t.category=?1")
    TodoList findByCategory(String category, Long UserId);

}
