package org.example.todo_list.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.todo_list.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsByName(@NotBlank String name);

    boolean existsById(@NonNull Long id);


    @Query("select t.id from Task t where t.todoList.category = ?1")
    List<Long> findTaskIdsByCategory(@NonNull String category);


    @Query("select t.id from Task t where t.todoList.id = ?1")
    List<Long> findIdsByTodoList_Id(Long id);
}
