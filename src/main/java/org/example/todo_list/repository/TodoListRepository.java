package org.example.todo_list.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.todo_list.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    @Query("select (count(t) > 0) from TodoList t where t.category = ?1")
    boolean existsByCategory(String category);

    @Query("select t.id from TodoList t where t.category = ?1")
    List<Long> getIdsByCategory(String category);

    @Query("select t.category from TodoList t")
    List<String> findAllCategories();

    @Override
    boolean existsById(Long id);

    @Override
    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query("update TodoList t set t.category = ?1 where t.id = ?2")
    int updateCategoryById(String category, Long id);


    TodoList findByCategory(@NotBlank(message = "任务类别不能为空") String category);
}
